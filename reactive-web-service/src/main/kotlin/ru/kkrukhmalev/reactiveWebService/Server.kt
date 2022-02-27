package ru.kkrukhmalev.reactiveWebService

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

typealias ServerRequestHandler = (HttpServerRequest<ByteBuf>, re: HttpServerResponse<ByteBuf>) -> Observable<Void>

abstract class Server {
    fun start(port: Int) = HttpServer
        .newServer(port)
        .start { req, resp -> processRequest(req, resp) }
        .awaitShutdown()

    private fun processRequest(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>,
    ): Observable<Void> {
        val matchedUri =
            requestsMap.keys.firstOrNull { 
                val decodedPath = request.decodedPath.replace("/+".toRegex(), "/").trimEnd('/')
                it.second == decodedPath && it.first == request.httpMethod 
            }
        return requestsMap[matchedUri]?.invoke(request, response)
            ?: response.setStatus(HttpResponseStatus.NOT_FOUND)
    }

    open val requestsMap: Map<Pair<HttpMethod, String>, ServerRequestHandler> = mapOf()
}

fun ServerBuilder.endpoint(httpMethod: HttpMethod, uri: String, handler: ServerRequestHandler) {
    this.requestsMap[(httpMethod to uri)] = handler
}

fun ServerBuilder.get(uri: String, handler: ServerRequestHandler) =
    endpoint(HttpMethod.GET, uri, handler)

fun ServerBuilder.post(uri: String, handler: ServerRequestHandler) =
    endpoint(HttpMethod.POST, uri, handler)

class ServerBuilder {
    val requestsMap: MutableMap<Pair<HttpMethod, String>, ServerRequestHandler> = mutableMapOf()
}

fun server(port: Int, build: ServerBuilder.() -> Unit) {
    val serverBuilder = ServerBuilder()
    serverBuilder.build()
    object : Server() {
        override val requestsMap = serverBuilder.requestsMap
    }.start(port)
}