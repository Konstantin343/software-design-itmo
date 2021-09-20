interface Cache<K, V> {
    /**
     * Adds pair (key, value) in cache by key
     * 
     * @param key key for store pair in cache, must be not null
     * @param value value for store it in cache by K, must be not null
     */
    fun add(key: K, value: V)

    /**
     * Adds pair (key, value) in cache by key
     *
     * @param key key for find associated value in cache, must be not null
     * 
     * @return value associated with passed key, if exists and null otherwise
     */
    fun get(key: K): V?

    /**
     * Checks if pair (key, value) exist in cache
     *
     * @param key key for find pair in cache, must be not null
     * @param value value for find pair in cache, must be not null
     */
    fun exists(key: K, value: V): Boolean

    /**
     * Returns all elements from cache as map, must have size lower than  or equal to maxSize
     */
    fun all(): Map<K, V>

    /**
     * Returns current size of cache, must be positive and lower than or equal to maxSize
     */
    val size: Int
}