package ru.kkrukhmalev.actors.stubServer

import com.xebialabs.restito.builder.stub.StubHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus

fun withStubServer(@Suppress("SameParameterValue") port: Int, callback: StubServer.() -> Unit) {
    val stubServer = StubServer(port)
    try {
        stubServer.run()
        stubServer.callback()
    } finally {
        stubServer.stop()
    }
}

fun StubServer.prepareHttpResponse(browser: String, n: Int, statusCode: Int = 200, delay: Int = 0) {
    val actions: MutableList<Action> = mutableListOf()
    if (delay > 0) {
        actions.add(Action.delay(delay))
    }
    actions.add(
        Action.custom {
            it.setStatus(HttpStatus.getHttpStatus(statusCode))
            it.outputStream.write(prepareResponseFor(it.request.parameterMap["q"]!!.first(), n).toByteArray())
            it
        }
    )
    StubHttp.whenHttp(this)
        .match(
            Condition.method(Method.GET),
            Condition.startsWithUri("/$browser/search")
        )
        .then(*actions.toTypedArray())
}

fun prepareResponseFor(text: String, n: Int): String {
    val stringBuilder = StringBuilder("{\"results\":[")
    for (i in 1..n) {
        stringBuilder.append("{\"text\":\"$text $i\"}")
        if (i != n)
            stringBuilder.append(",")
    }
    stringBuilder.append("]}")
    return stringBuilder.toString()
}
