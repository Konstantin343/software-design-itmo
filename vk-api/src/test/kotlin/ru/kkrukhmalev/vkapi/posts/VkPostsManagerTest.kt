package ru.kkrukhmalev.vkapi.posts

import org.mockito.Matchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.mockito.MockitoAnnotations
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import ru.kkrukhmalev.vkapi.client.VkApiClient
import java.time.Duration

class VkPostsManagerTest {
    private lateinit var vkPostsManager: VkPostsManager
    
    @Mock
    lateinit var vkApiClient: VkApiClient
    
    @BeforeMethod
    fun initializeMocks() {
        MockitoAnnotations.initMocks(this)
        vkPostsManager = VkPostsManager(vkApiClient)
    }

    @Test
    fun getEmptyPostsCount() {
        Assert.assertEquals(vkPostsManager.getPostsStats("query", 5), listOf(0, 0, 0, 0, 0))
    }
    
    @DataProvider(name = "negativeData")
    fun negativeData() = arrayOf(-3, 0, 25)

    @Test(dataProvider = "negativeData")
    fun getPostsCountForBadHours(hours: Int) {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            vkPostsManager.getPostsStats("query", hours)
        }
    }
    
    @Test
    fun getPostsCount() {
        `when`(vkApiClient.getPosts(safeEq("#query"), anyLong(), anyLong()))
            .thenReturn(createAnswer(System.currentTimeMillis() / 1000))

        Assert.assertEquals(vkPostsManager.getPostsStats("query", 5), listOf(1, 1, 2, 2, 1))
    }
    
    private fun createAnswer(currentTime: Long) = listOf(
        VkPost(1, 2, currentTime - 5.hoursToSeconds(), "text1"),
        VkPost(2, 2, currentTime - 3.hoursToSeconds(), "text1"),
        VkPost(100, -50, currentTime - 3.hoursToSeconds(), "text3"),
        VkPost(101, -50, currentTime - 4.hoursToSeconds(), "text3"),
        VkPost(100000001, 17333, currentTime -2.hoursToSeconds(), "text3"),
        VkPost(100000002, 17333, currentTime -2.hoursToSeconds(), "text3"),
        VkPost(100000003, 17333, currentTime -1.hoursToSeconds(), "text3"),
        VkPost(123, 32, currentTime -10.hoursToSeconds(), "text4"),
    )
    
    private fun Int.hoursToSeconds() = this * 60 * 60
    
    private fun <T : Any> safeEq(@Suppress("SameParameterValue") value: T): T = eq(value) ?: value
}