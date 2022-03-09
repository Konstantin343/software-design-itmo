package ru.kkrukhmalev.fitnessCentre.events.turnstile

import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visit
import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visits
import java.time.LocalDateTime

class EnterEvent(time: LocalDateTime, number: Long) : TurnstileEvent(time, number) {
    override fun applyTo(target: Visits) = target.also {
        val lastVisit = it.visits.lastOrNull { v -> v.number == memberNumber }
        if (lastVisit == null || lastVisit.end != null)
            it.visits.add(Visit().apply {
                start = time
                number = memberNumber
            })
    }
    
    
}