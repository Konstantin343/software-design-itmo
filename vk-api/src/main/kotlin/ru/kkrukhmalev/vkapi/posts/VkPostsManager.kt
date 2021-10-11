package ru.kkrukhmalev.vkapi.posts

import ru.kkrukhmalev.vkapi.client.VkApiClient

class VkPostsManager(
    private val client: VkApiClient
) {
    fun getPostsStats(hashtag: String, hours: Int): List<Int> {
        if (hours !in 1..24)
            throw IllegalArgumentException("hours must be between 1 and 24")
        
        val endTime = System.currentTimeMillis() / 1000
        val startTime = endTime - hours * 60 * 60
        
        val posts = client.getPosts("#${hashtag}", startTime, endTime)
        val stats = MutableList(hours) { 0 }
        for (post in posts) {
            val index = hours - ((endTime - post.unixTime) / 60 / 60).toInt()
            if (index < 0 || index > hours) 
                continue
            stats[index]++
        }
        return stats
    }
}