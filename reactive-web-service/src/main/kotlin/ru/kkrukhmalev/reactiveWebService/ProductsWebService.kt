package ru.kkrukhmalev.reactiveWebService

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoDatabase
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.bson.Document
import ru.kkrukhmalev.reactiveWebService.currency.Currency
import ru.kkrukhmalev.reactiveWebService.currency.CurrencyCalculator
import ru.kkrukhmalev.reactiveWebService.currency.StableCurrencyCalculator
import ru.kkrukhmalev.reactiveWebService.mongo.collection
import ru.kkrukhmalev.reactiveWebService.mongo.mongoDb
import ru.kkrukhmalev.reactiveWebService.mongo.unique
import ru.kkrukhmalev.reactiveWebService.rxUtils.result
import ru.kkrukhmalev.reactiveWebService.rxUtils.toPrettyJson
import ru.kkrukhmalev.reactiveWebService.rxUtils.withValidatedBody
import ru.kkrukhmalev.reactiveWebService.validator.BodyValidator
import rx.Observable

class ProductsWebService(
    private val port: Int,
    private val dbHost: String,
    private val dbPort: Int,
    private val dbName: String,
) {
    companion object {
        const val USERS_COLLECTION = "users"
        const val USERS_LOGIN = "login"
        const val USERS_NAME = "name"
        const val USERS_CURRENCY = "currency"

        const val PRODUCTS_COLLECTION = "products"
        const val PRODUCTS_NAME = "name"
        const val PRODUCTS_DESCRIPTION = "description"
        const val PRODUCTS_OWNER = "owner"
        const val PRODUCTS_PRICE = "price"

        const val AUTH = "auth"
    }

    private val usersValidator = BodyValidator(
        mapOf(
            USERS_NAME to null,
            USERS_LOGIN to null,
            USERS_CURRENCY to Currency.values().joinToString("|").toRegex()
        )
    )

    private val productsValidator = BodyValidator(
        mapOf(
            PRODUCTS_NAME to null,
            PRODUCTS_DESCRIPTION to null,
            PRODUCTS_OWNER to null,
            PRODUCTS_PRICE to "\\d+(\\.\\d{2})?".toRegex()
        )
    )

    private val currencyCalculator: CurrencyCalculator = StableCurrencyCalculator()

    fun start() {
        val db = mongoDb(dbHost, dbPort, dbName) {
            collection(USERS_COLLECTION) {
                unique(USERS_LOGIN)
            }
            collection(PRODUCTS_COLLECTION) {
                unique(PRODUCTS_NAME, PRODUCTS_DESCRIPTION, PRODUCTS_OWNER)
            }
        }
        server(port) {
            post("/users/register") { req, resp ->
                val result = req.content.map {
                    usersValidator.withValidatedBody(it, resp) { user ->
                        db.getCollection(USERS_COLLECTION)
                            .insertOne(user)
                            .map {
                                resp.result(true)
                            }
                            .onErrorReturn { e ->
                                resp.result(false, HttpResponseStatus.BAD_REQUEST, e.localizedMessage)
                            }
                    }
                }.flatMap { it }
                resp.writeString(result)
            }
            get("/users") { req, resp ->
                val result = checkAuth(db, req, resp) {
                    db.getCollection(USERS_COLLECTION)
                        .find()
                        .toObservable()
                        .map {
                            it.toPrettyJson()
                        }
                }
                resp.writeString(result)
            }
            post("/products/add") { req, resp ->
                val result = checkAuth(db, req, resp) { auth ->
                    req.content.map {
                        productsValidator.withValidatedBody(it, resp) { product ->
                            if (auth[USERS_LOGIN] != product[PRODUCTS_OWNER].toString()) {
                                return@withValidatedBody Observable.fromCallable {
                                    resp.result(
                                        false, HttpResponseStatus.FORBIDDEN,
                                        "Can't add product for other user"
                                    )
                                }
                            }
                            product[PRODUCTS_PRICE] = currencyCalculator.calculate(
                                product[PRODUCTS_PRICE].toString().toDouble(),
                                Currency.valueOf(auth[USERS_CURRENCY].toString()),
                                Currency.USD
                            )
                            db.getCollection(PRODUCTS_COLLECTION)
                                .insertOne(product)
                                .map {
                                    resp.result(true)
                                }
                                .onErrorReturn { e ->
                                    resp.result(false, HttpResponseStatus.BAD_REQUEST, e.localizedMessage)
                                }
                        }
                    }.flatMap { it }
                }
                resp.writeString(result)
            }
            get("/products") { req, resp ->
                val result = checkAuth(db, req, resp) { auth ->
                    db.getCollection(PRODUCTS_COLLECTION)
                        .find()
                        .toObservable()
                        .map {
                            calculateProductPrice(it, auth)
                            it.toPrettyJson()
                        }
                }
                resp.writeString(result)
            }
            get("/products/my") { req, resp ->
                val result = checkAuth(db, req, resp) { auth ->
                    db.getCollection(PRODUCTS_COLLECTION)
                        .find(Filters.eq(PRODUCTS_OWNER, auth[USERS_LOGIN]))
                        .toObservable()
                        .map {
                            calculateProductPrice(it, auth)
                            it.toPrettyJson()
                        }
                }
                resp.writeString(result)
            }
            get("/me") { req, resp ->
                val result = checkAuth(db, req, resp) { auth ->
                    Observable.fromCallable { auth.toPrettyJson() }
                }
                resp.writeString(result)
            }
        }
    }

    private fun calculateProductPrice(document: Document, auth: Document) {
        document[PRODUCTS_PRICE] = currencyCalculator.calculate(
            document[PRODUCTS_PRICE].toString().toDouble(),
            Currency.USD,
            Currency.valueOf(auth[USERS_CURRENCY].toString())
        )
        document[USERS_CURRENCY] = auth[USERS_CURRENCY].toString()
    }

    private fun checkAuth(
        db: MongoDatabase,
        req: HttpServerRequest<ByteBuf>,
        resp: HttpServerResponse<ByteBuf>,
        onSuccess: (Document) -> Observable<String>,
    ): Observable<String> {
        val auth = req.queryParameters[AUTH]?.firstOrNull()
            ?: return Observable.fromCallable {
                resp.result(false, HttpResponseStatus.UNAUTHORIZED, "No '$AUTH' parameter")
            }
        val count = db.getCollection(USERS_COLLECTION)
            .count(Filters.eq(USERS_LOGIN, auth))
            .map {
                if (it <= 0) {
                    return@map Observable.fromCallable {
                        resp.result(false, HttpResponseStatus.UNAUTHORIZED, "No user '$auth'")
                    }
                }
                return@map Observable.fromCallable { "" }
            }
        val user = db.getCollection(USERS_COLLECTION)
            .find(Filters.eq(USERS_LOGIN, auth))
            .toObservable()
            .map {
                onSuccess(it)
            }
        return Observable.merge(count, user).flatMap { it }
    }
}