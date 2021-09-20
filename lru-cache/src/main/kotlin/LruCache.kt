import java.util.*
import kotlin.collections.HashMap

/**
 * Data structure represents LRU cache, using double linked list and hash map
 *
 * @param maxSize initial max size of LRU cache, must be greater than 0
 */
class LruCache<K, V>(private val maxSize: Int) : Cache<K, V> {
    private val hashMap = HashMap<K, V>()
    private val recentlyUsed = LinkedList<K>()

    private var currentSize: Int = 0

    init {
        assert(maxSize > 0) { "Initial max size must be > 0, actual: $maxSize" }
    }

    override fun add(key: K, value: V) {
        assertKeyNotNull(key)
        assertValueNotNull(value)
        
        val startSize = currentSize
        
        if (hashMap.containsKey(key)) {
            hashMap[key] = value
            recentlyUsed.addFirst(key)
            recentlyUsed.remove(key)
        } else {
            hashMap[key] = value
            recentlyUsed.addFirst(key)
            if (currentSize == maxSize) {
                hashMap.remove(recentlyUsed.last)
                recentlyUsed.removeLast()
            } else {
                currentSize++
            }
        }
        
        assertSize(currentSize)
        assert(currentSize >= startSize) { "Size cannot be reduced" } 
    }

    override fun get(key: K): V? {
        assertKeyNotNull(key)
        
        val value = hashMap[key] ?: return null
        recentlyUsed.remove(key)
        recentlyUsed.addFirst(key)
        return value
    }

    override fun exists(key: K, value: V): Boolean {
        assertKeyNotNull(key)
        assertValueNotNull(value)
        return get(key) == value
    }


    override fun all(): Map<K, V> {
        val result = hashMap.toMap()
        assertSize(result.size)
        return result
    }

    override val size: Int
        get() {
            assertSize(currentSize)
            return currentSize
        }

    private fun assertSize(sizeToCheck: Int) =
        assert(sizeToCheck in 0..maxSize) { "currentSize must be > 0 and <= $maxSize, actual: $currentSize" }

    private fun assertKeyNotNull(key: K) =
        assert(key != null) { "key must be not null" }

    private fun assertValueNotNull(value: V) =
        assert(value != null) { "value must be not null" }
}