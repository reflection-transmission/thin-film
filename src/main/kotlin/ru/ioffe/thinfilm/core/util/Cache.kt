package ru.ioffe.thinfilm.core.util

class Cache<T> {

    private val cache = ArrayList<T>(1)

    fun available(): Boolean = cache[0] != null

    fun get(): T = cache[0]

    fun put(t: T) {
        cache[0] = t
    }

}