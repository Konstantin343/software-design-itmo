package ru.kkrukhmalev.actors.api

import ru.kkrukhmalev.actors.http.TextUrlReader
import ru.kkrukhmalev.actors.model.SearchResult
import java.net.URL
import java.net.URLEncoder

abstract class SearchApi(
    private val host: String,
    val name: String,
) {
    abstract fun topNResults(query: String, n: Int): List<SearchResult>

    protected fun buildSearchResult(text: String) = SearchResult(text, name)

    protected val urlReader = TextUrlReader()

    protected fun createRequestUrl(
        @Suppress("SameParameterValue") apiMethod: String,
        parameters: Map<String, Any>,
    ): URL {
        val parametersString = parameters.entries.joinToString("&") {
            "${it.key}=${URLEncoder.encode(it.value.toString(), Charsets.UTF_8)}"
        }
        return URL("http://$host/$apiMethod?$parametersString")
    }
}