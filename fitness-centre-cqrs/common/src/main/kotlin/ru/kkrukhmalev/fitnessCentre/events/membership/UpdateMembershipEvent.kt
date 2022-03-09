package ru.kkrukhmalev.fitnessCentre.events.membership

import ru.kkrukhmalev.fitnessCentre.model.membership.GymMembership
import java.time.LocalDate

class UpdateMembershipEvent(
    number: Long,
    val until: LocalDate,
) : MembershipEvent(number) {
    override fun applyTo(target: GymMembership) = target.also {
        if (number == it.number)
            it.until = until
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UpdateMembershipEvent

        if (until != other.until) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        return until.hashCode() * 31 + number.hashCode()
    }
    
    override fun toString() =
        "UpdateMembershipEvent(number='$number', until=$until)"
}