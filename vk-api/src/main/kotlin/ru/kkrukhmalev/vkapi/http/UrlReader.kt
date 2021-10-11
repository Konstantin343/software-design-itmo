package ru.kkrukhmalev.vkapi.http

import java.net.URL

interface UrlReader<T> {
    fun read(url: URL): T
}