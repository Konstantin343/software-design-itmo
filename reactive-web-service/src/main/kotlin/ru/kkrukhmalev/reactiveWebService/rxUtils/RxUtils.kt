package ru.kkrukhmalev.reactiveWebService.rxUtils

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import org.bson.Document
import org.bson.json.JsonWriterSettings
import ru.kkrukhmalev.reactiveWebService.validator.BodyValidator
import rx.Observable

fun BodyValidator.withValidatedBody(
    body: ByteBuf,
    response: HttpServerResponse<ByteBuf>,
    onSuccess: (Document) -> Observable<String>,
): Observable<String> {
    val document = Document.parse(body.toString(Charsets.UTF_8))
    this.validate(document)?.let {
        return Observable.fromCallable {
            response.result(false, HttpResponseStatus.BAD_REQUEST, it)
        }
    }
    return onSuccess(document)
}

fun HttpServerResponse<*>.result(
    success: Boolean,
    status: HttpResponseStatus = HttpResponseStatus.OK,
    message: String = "",
): String {
    this.status = status
    return "{\"success\": \"$success\", \"message\": \"$message\"}\n"
}

fun Document.toPrettyJson(): String {
    this.remove("_id")
    return "${this.toJson(JsonWriterSettings.builder().indent(true).build())},\n"
}