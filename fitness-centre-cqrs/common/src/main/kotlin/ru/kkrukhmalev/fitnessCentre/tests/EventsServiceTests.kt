package ru.kkrukhmalev.fitnessCentre.tests

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import ru.kkrukhmalev.fitnessCentre.events.Event
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsReadInMemoryRepository
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsWriteInMemoryRepository
import ru.kkrukhmalev.fitnessCentre.events.repository.InMemoryEventsRepositories
import ru.kkrukhmalev.fitnessCentre.service.EventsService
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class EventsServiceTests<T : EventsService>(builder: (InMemoryEventsRepositories) -> T) {
    companion object {
        const val PORT = 8080
    }
    
    fun HttpResponse<String>.shouldResponse(content: String) {
        Assertions.assertEquals(content, this.body())
    }

    fun eventsShouldBe(
        vararg events: Event<*>,
    ) {
        Assertions.assertArrayEquals(
            events.toList().toTypedArray(),
            eventsRepositories.readRepository.getEvents(0).toTypedArray()
        )
    }

    private val eventsRepositories =
        InMemoryEventsRepositories(EventsWriteInMemoryRepository(), EventsReadInMemoryRepository())

    private val service = builder(eventsRepositories)

    private var executor: Thread? = null

    private val client = HttpClient.newHttpClient()

    fun withEvents(vararg events: Event<*>, action: () -> Unit) {
        try {
            service.clearState()
            eventsRepositories.writeRepository.apply {
                events.forEach {
                    publishEvent(it)
                }
            }
            action()
        } finally {
            eventsRepositories.clearAll()
        }
    }

    fun request(uri: String, vararg parameters: Pair<String, Any>): HttpResponse<String> {
        val url = "http://localhost:$PORT$uri?${parameters.joinToString("&") { "${it.first}=${it.second}" }}"
        return client.send(HttpRequest.newBuilder(URI(url)).GET().build(), HttpResponse.BodyHandlers.ofString())
    }

    private fun waitUntilServiceStart() {
        while (true) {
            try {
                request("")
                break
            } catch (_: Exception) {
            }
        }
    }
    
    @BeforeEach
    fun startService() {
        if (executor == null) {
            executor = thread {
                service.start(PORT)
            }
            waitUntilServiceStart()
        }
    }
}