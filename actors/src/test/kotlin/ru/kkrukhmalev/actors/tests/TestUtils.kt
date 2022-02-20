package ru.kkrukhmalev.actors.tests

import org.junit.jupiter.api.Assertions
import ru.kkrukhmalev.actors.SearchResultsAggregator
import ru.kkrukhmalev.actors.model.SearchRequest
import ru.kkrukhmalev.actors.model.SearchResult
import java.time.Duration

fun SearchResultsAggregator.request(query: String, resultsCount: Int, timeout: Duration = Duration.ofSeconds(30)): SearchRequest {
    val request = SearchRequest(query, resultsCount, mutableListOf(), timeout)
    this.aggregate(request)
    while (!request.complete) {
        Thread.sleep(100)
    }

    return request
}

fun SearchRequest.shouldResponse(expected: Collection<SearchResult>) {
    Assertions.assertArrayEquals(
        expected.sortedBy { it.source }.toTypedArray(),
        this.result.sortedBy { it.source }.toTypedArray()
    )
}

annotation class StubServerRule(
    val browser: String,
    val resultsCount: Int,
    val statusCode: Int = 200,
    val delay: Int = 0,
)

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
annotation class StubServerRules(
    vararg val rules: StubServerRule,
)