package ru.kkrukhmalev.actors.tests

import org.junit.jupiter.api.Test
import ru.kkrukhmalev.actors.model.SearchResult
import java.time.Duration

class SearchAggregatorTests : BaseTestWithStubServer() {
    @Test
    @StubServerRules(
        StubServerRule("yandex", 5),
        StubServerRule("google", 5),
        StubServerRule("bing", 5)
    )
    fun getAllResponses() =
        request(
            "some search query",
            3
        ).shouldResponse(
            listOf(
                SearchResult("some search query 1", "Google"),
                SearchResult("some search query 2", "Google"),
                SearchResult("some search query 3", "Google"),
                SearchResult("some search query 1", "Yandex"),
                SearchResult("some search query 2", "Yandex"),
                SearchResult("some search query 3", "Yandex"),
                SearchResult("some search query 1", "Bing"),
                SearchResult("some search query 2", "Bing"),
                SearchResult("some search query 3", "Bing")
            )
        )

    @Test
    @StubServerRules(
        StubServerRule("yandex", 5, 2000),
        StubServerRule("google", 5),
        StubServerRule("bing", 5)
    )
    fun oneApiHangs() =
        request(
            "some search query",
            3,
            Duration.ofMillis(1000)
        ).shouldResponse(
            listOf(
                SearchResult("some search query 1", "Google"),
                SearchResult("some search query 2", "Google"),
                SearchResult("some search query 3", "Google"),
                SearchResult("some search query 1", "Bing"),
                SearchResult("some search query 2", "Bing"),
                SearchResult("some search query 3", "Bing")
            )
        )

    @Test
    @StubServerRules(
        StubServerRule("yandex", 5, delay = 2000),
        StubServerRule("google", 5, delay = 1000),
        StubServerRule("bing", 5)
    )
    fun oneApiHangsAndOneLateResponse() =
        request(
            "some search query",
            3,
            Duration.ofMillis(1500)
        ).shouldResponse(
            listOf(
                SearchResult("some search query 1", "Google"),
                SearchResult("some search query 2", "Google"),
                SearchResult("some search query 3", "Google"),
                SearchResult("some search query 1", "Bing"),
                SearchResult("some search query 2", "Bing"),
                SearchResult("some search query 3", "Bing")
            )
        )

    @Test
    @StubServerRules(
        StubServerRule("yandex", 5),
        StubServerRule("google", 5),
        StubServerRule("bing", 5, statusCode = 400)
    )
    fun oneApiReturnsError() =
        request(
            "some search query",
            3
        ).shouldResponse(
            listOf(
                SearchResult("some search query 1", "Google"),
                SearchResult("some search query 2", "Google"),
                SearchResult("some search query 3", "Google"),
                SearchResult("some search query 1", "Yandex"),
                SearchResult("some search query 2", "Yandex"),
                SearchResult("some search query 3", "Yandex")
            )
        )

    @Test
    @StubServerRules(
        StubServerRule("yandex", 2),
        StubServerRule("google", 1),
        StubServerRule("bing", 5)
    )
    fun requestMoreResultsThanExists() =
        request(
            "some search query",
            3
        ).shouldResponse(
            listOf(
                SearchResult("some search query 1", "Google"),
                SearchResult("some search query 1", "Yandex"),
                SearchResult("some search query 2", "Yandex"),
                SearchResult("some search query 1", "Bing"),
                SearchResult("some search query 2", "Bing"),
                SearchResult("some search query 3", "Bing"),
            )
        )
}