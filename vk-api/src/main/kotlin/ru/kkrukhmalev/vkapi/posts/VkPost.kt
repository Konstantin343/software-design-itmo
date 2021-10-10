package ru.kkrukhmalev.vkapi.posts

data class VkPost(
    val id: Long,
    val owner_id: Long,
    val unixTime: Long,
    val text: String
) 