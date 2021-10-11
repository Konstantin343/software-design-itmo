package ru.kkrukhmalev.vkapi.http

import org.testng.Assert
import org.testng.annotations.Test
import ru.kkrukhmalev.vkapi.base.BaseTestWithVkApiHost
import java.net.URL

class UrlReaderTest : BaseTestWithVkApiHost() {
    @Test
    fun readAsText() {
        val urlReader = TextUrlReader()
        val response = urlReader.read(
            URL("https://$host:$port/method/newsfeed.search?q=1&start_time=1633814137&end_time=1633900537&v=$version&access_token=$accessToken")
        )
        Assert.assertTrue(response.isNotEmpty())
    }
}