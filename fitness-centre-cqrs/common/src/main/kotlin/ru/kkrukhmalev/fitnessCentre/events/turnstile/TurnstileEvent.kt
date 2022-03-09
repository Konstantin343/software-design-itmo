package ru.kkrukhmalev.fitnessCentre.events.turnstile

import ru.kkrukhmalev.fitnessCentre.events.Event
import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visits
import java.time.LocalDateTime

abstract class TurnstileEvent(val time: LocalDateTime, val memberNumber: Long) : Event<Visits>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TurnstileEvent

        if (time != other.time) return false
        if (memberNumber != other.memberNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + memberNumber.hashCode()
        return result
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}(time=$time, memberNumber=$memberNumber)"
    }
}