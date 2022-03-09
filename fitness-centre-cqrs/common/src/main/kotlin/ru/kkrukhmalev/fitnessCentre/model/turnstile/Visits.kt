package ru.kkrukhmalev.fitnessCentre.model.turnstile

import java.time.Duration
import java.time.LocalDate

class Visits {
    val visits: MutableList<Visit> = mutableListOf()

    private fun visitsBetween(from: LocalDate, to: LocalDate, memberNumber: Long? = null): List<Visit> {
        return visits.filter {
            (memberNumber == null || it.number == memberNumber)
                    && it.start!!.toLocalDate() in from..to
        }.sortedBy { it.start!! }
    }

    fun visitsStats(from: LocalDate, to: LocalDate, memberNumber: Long? = null) =
        visitsBetween(from, to, memberNumber).groupBy { it.start!!.toLocalDate() }

    fun averageVisitTime(from: LocalDate, to: LocalDate, memberNumber: Long? = null): Duration {
        val visitsFromTo = visitsBetween(from, to, memberNumber)
        return Duration.ofSeconds(visitsFromTo.sumOf { it.duration.toSeconds() } / visitsFromTo.size)
    }
}