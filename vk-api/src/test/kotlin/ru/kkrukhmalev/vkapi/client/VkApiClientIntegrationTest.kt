package ru.kkrukhmalev.vkapi.client

import org.testng.Assert
import org.testng.annotations.Test
import ru.kkrukhmalev.vkapi.base.BaseTestWithVkApiHost

class VkApiClientIntegrationTest : BaseTestWithVkApiHost() {
    @Test
    fun getPosts() {
        val vkApiClient = createVkApiClient()
        val endTime = System.currentTimeMillis() / 1000
        val startTime = endTime - 24 * 60 * 60
        val posts = vkApiClient.getPosts("1", startTime, endTime)
        Assert.assertTrue(posts.isNotEmpty())
    }

    private fun createVkApiClient() =
        VkApiClient(host, port, accessToken, version)
}