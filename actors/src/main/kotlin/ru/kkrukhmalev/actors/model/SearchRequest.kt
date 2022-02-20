package ru.kkrukhmalev.actors.model

import java.time.Duration

data class SearchRequest(
    val text: String,
    val count: Int,
    val result: MutableList<SearchResult>,
    val timeout: Duration,
    var complete: Boolean = false
)