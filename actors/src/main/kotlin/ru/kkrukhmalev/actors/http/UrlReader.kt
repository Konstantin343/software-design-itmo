package ru.kkrukhmalev.actors.http

import java.net.URL

interface UrlReader<T> {
    fun read(url: URL): T
}