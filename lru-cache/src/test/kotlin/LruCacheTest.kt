import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.Integer.min
import java.lang.Integer.max

class LruCacheTest {
    @Test
    fun testSimpleAdd() {
        val cache = LruCache<String, String>(5)
        val (key, value) = "qwerty" to "abacaba"
        cache.add(key, value)
        
        Assert.assertEquals(cache.size, 1)
        Assert.assertEquals(cache.get(key), value)
        Assert.assertEquals(cache.all(), mapOf(key to value))
        Assert.assertTrue(cache.exists(key, value))
    }

    @Test
    fun testManyAdds() {
        val cache = LruCache<String, String>(5)
        for (i in 0..10) {
            val (key, value) = i.toString() to (i + 5).toString()
            cache.add(key, value)

            Assert.assertEquals(cache.size, min(5, i + 1))
            Assert.assertEquals(cache.get(key), value)
            Assert.assertEquals(cache.all(), (max(0, i - 4)..i).associate { it.toString() to (it + 5).toString() })
            Assert.assertTrue(cache.exists(key, value))
        }
        
        for (i in 0..5) {
            val (key, value) = i.toString() to (i + 5).toString()
            Assert.assertFalse(cache.exists(key, value))
            Assert.assertNull(cache.get(key))
        }
    }


    @Test
    fun testAddSameValues() {
        val cache = LruCache<Int, Int>(5)
        for (i in 0..10) {
            val (key, value) = i % 2 to i
            cache.add(key, value)

            Assert.assertEquals(cache.size, min(2, i + 1))
            Assert.assertEquals(cache.get(key), value)
            Assert.assertEquals(cache.all(), (max(0, i - 1)..i).associateBy { it % 2 })
            Assert.assertTrue(cache.exists(key, value))
        }
    }
    
    @DataProvider(name = "badMaxSizes")
    fun maxSizes() = arrayOf(0, -1, -5)
    
    @Test(dataProvider = "badMaxSizes")
    fun testInitialMaxSize(maxSize: Int) {
        Assert.assertThrows(AssertionError::class.java) {
            LruCache<String, String>(maxSize)
        }
    }

    @Test
    fun testNullArguments() {
        val cache = LruCache<Int?, Int?>(10)
        Assert.assertThrows(AssertionError::class.java) {
            cache.add(null, 1)
        }

        Assert.assertThrows(AssertionError::class.java) {
            cache.add(1, null)
        }

        Assert.assertThrows(AssertionError::class.java) {
            cache.get(null)
        }

        Assert.assertThrows(AssertionError::class.java) {
            cache.exists(null, 1)
        }

        Assert.assertThrows(AssertionError::class.java) {
            cache.exists(1, null)
        }
    }
}