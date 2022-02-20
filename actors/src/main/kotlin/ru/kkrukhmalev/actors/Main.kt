package ru.kkrukhmalev.actors

import ru.kkrukhmalev.actors.api.BingSearchApi
import ru.kkrukhmalev.actors.api.GoogleSearchApi
import ru.kkrukhmalev.actors.api.YandexSearchApi
import ru.kkrukhmalev.actors.model.SearchRequest
import ru.kkrukhmalev.actors.model.SearchResult
import ru.kkrukhmalev.actors.stubServer.prepareHttpResponse
import ru.kkrukhmalev.actors.stubServer.withStubServer
import java.time.Duration

/**
 * Usage: "<search text>" <size of top queries> <timeout in seconds>
 */
fun main(args: Array<String>) {
    withStubServer(8081) {
        prepareHttpResponse("yandex", 2)
        prepareHttpResponse("google", 10)
        prepareHttpResponse("bing", 3, delay = 3500)

        val searchApis = listOf(
            YandexSearchApi("localhost:8081"),
            GoogleSearchApi("localhost:8081"),
            BingSearchApi("localhost:8081")
        )
        val searchResultsAggregator = SearchResultsAggregator(searchApis)
        val request = SearchRequest(args[0], args[1].toInt(), mutableListOf(), Duration.ofSeconds(args[2].toLong()))
        searchResultsAggregator.aggregate(request)

        while (!request.complete) {
            Thread.sleep(100)
        }
        printResults(request.result)

        searchResultsAggregator.stop()
    }
}

fun printResults(results: List<SearchResult>) {
    println("==============")
    println("Results:")
    results.groupBy { it.source }.forEach {
        println("- ${it.key}")
        it.value.forEach { sr ->
            println("  ${sr.text}")
        }
    }
    println("==============")
}