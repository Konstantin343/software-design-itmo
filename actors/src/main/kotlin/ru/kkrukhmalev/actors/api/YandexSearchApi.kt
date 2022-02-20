package ru.kkrukhmalev.actors.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import ru.kkrukhmalev.actors.model.SearchResult

class YandexSearchApi(
    host: String,
) : SearchApi(host,"Yandex") {
    override fun topNResults(query: String, n: Int): List<SearchResult> {
        val response = urlReader.read(createRequestUrl("yandex/search", mapOf("q" to query)))
        return JsonParser().parse(response).asJsonObject["results"].asJsonArray.map {
            it as JsonObject
            buildSearchResult(it["text"].asString)
        }.take(n)
    }
}