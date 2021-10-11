package ru.kkrukhmalev.vkapi.client

import ru.kkrukhmalev.vkapi.http.TextUrlReader
import ru.kkrukhmalev.vkapi.posts.VkPost
import ru.kkrukhmalev.vkapi.posts.VkPostsResponseParser
import java.net.URL
import java.net.URLEncoder

open class VkApiClient(
    private val host: String,
    private val port: Int,
    private val accessToken: String,
    private val version: String,
) {
    companion object {
        const val NEWSFEED_SEARCH = "newsfeed.search"
        
        const val VERSION = "v"
        const val ACCESS_TOKEN = "access_token"
        const val QUERY = "q"
        const val START_TIME = "start_time"
        const val END_TIME = "end_time"
    }
    
    private val urlReader = TextUrlReader()
    private val postResponseParser = VkPostsResponseParser()

    private val requiredParameters
        get() = mapOf(
            VERSION to version,
            ACCESS_TOKEN to accessToken
        )

    private fun createRequestUrl(
        @Suppress("SameParameterValue") apiMethod: String,
        parameters: Map<String, Any>,
    ): URL {
        val parametersString = ((parameters + requiredParameters).entries).joinToString("&") {
            "${it.key}=${URLEncoder.encode(it.value.toString(), Charsets.UTF_8)}"
        }
        return URL("https://${host}:${port}/method/${apiMethod}?${parametersString}")
    }

    open fun getPosts(query: String, startTime: Long, endTime: Long): List<VkPost> {
        val parameters = mapOf(
            QUERY to query,
            START_TIME to startTime,
            END_TIME to endTime
        )
        val url = createRequestUrl(NEWSFEED_SEARCH, parameters)
        val response = urlReader.read(url)
        return postResponseParser.parseResponse(response)
    }
}