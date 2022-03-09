package ru.kkrukhmalev.fitnessCentre.events.turnstile

import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visits
import java.time.LocalDateTime

class ExitEvent(time: LocalDateTime, number: Long) : TurnstileEvent(time, number) {
    override fun applyTo(target: Visits) = target.also {
        val lastVisit = it.visits.lastOrNull { v -> v.number == memberNumber } ?: return@also
        if (lastVisit.end == null)
            lastVisit.end = time
    }
}