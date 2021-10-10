package ru.kkrukhmalev.vkapi.posts

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

class VkPostsResponseParser {
    fun parseResponse(response: String): List<VkPost> {
        try {
            return JsonParser().parse(response)
                .asJsonObject["response"]
                .asJsonObject["items"]
                .asJsonArray.map {
                    it as JsonObject
                    VkPost(
                        id = it["id"].asLong,
                        owner_id = it["owner_id"].asLong,
                        unixTime = it["date"].asLong,
                        text = it["text"].asString
                    )
                }
        } catch (e: Exception) {
            when (e) {
                is JsonParseException,
                is JsonSyntaxException,
                -> throw IllegalArgumentException("response must be a correct JSON ", e)
                else -> throw e
            }
        }
    }
}