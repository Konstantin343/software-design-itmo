package ru.kkrukhmalev.fitnessCentre.model.turnstile

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class Visit {
    var number: Long? = null
    var start: LocalDateTime? = null
    var end: LocalDateTime? = null

    val duration: Duration
        get() {
            val secondsStart = start!!.toEpochSecond(ZoneOffset.ofTotalSeconds(0))
            val secondsEnd = (end ?: LocalDateTime.now().withNano(0)).toEpochSecond(ZoneOffset.ofTotalSeconds(0))
            return Duration.ofSeconds(secondsEnd - secondsStart)
        }
}