package ru.kkrukhmalev.vkapi.http

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.semantics.Condition.startsWithUri
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.testng.Assert
import org.testng.annotations.Test
import java.io.UncheckedIOException
import java.net.URL

class UrlReaderWithStubServerTest {
    private val port = 32453
    private val urlReader = TextUrlReader()
    
    private val url: URL
        get() = URL("http://localhost:$port/posts")

    @Test
    fun readAsTextMultiline() {
        val json = """
            {
                "response": {
                "items": [],
                "count": 0,
                "total_count": 0
                }
            }"""
        
        withStubServer(port) {
            whenHttp(this)
                .match(method(Method.GET), startsWithUri("/posts"))
                .then(stringContent(json))
            val result = urlReader.read(url)
            Assert.assertEquals(result, json + "\n")
        }
    }

    @Test
    fun readAsTextWithNotFoundError() {
        Assert.assertThrows(UncheckedIOException::class.java) {
            withStubServer(port) { 
                whenHttp(this)
                    .match(method(Method.GET), startsWithUri("/posts"))
                    .then(status(HttpStatus.NOT_FOUND_404))
                urlReader.read(url)
            }
        }
    }

    private fun withStubServer(@Suppress("SameParameterValue") port: Int, callback: StubServer.() -> Unit) {
        val stubServer = StubServer(port)
        try {
            stubServer.run()
            stubServer.callback()
        } finally {
            stubServer.stop()
        }
    }
}