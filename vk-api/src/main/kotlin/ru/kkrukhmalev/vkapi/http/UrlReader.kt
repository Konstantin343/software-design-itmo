package ru.kkrukhmalev.vkapi.http

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.net.URL

class UrlReader {
    fun readAsText(url: URL): String {
        try {
            BufferedReader(InputStreamReader(url.openStream())).use { 
                val buffer = StringBuilder()
                var inputLine: String?
                while ((it.readLine().also { line -> inputLine = line }) != null) {
                    buffer.appendLine(inputLine)
                }
                return buffer.toString()
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}
