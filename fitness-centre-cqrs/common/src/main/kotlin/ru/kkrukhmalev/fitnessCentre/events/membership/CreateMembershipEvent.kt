package ru.kkrukhmalev.fitnessCentre.events.membership

import ru.kkrukhmalev.fitnessCentre.model.membership.GymMembership
import java.time.LocalDate

class CreateMembershipEvent(
    number: Long,
    var member: String,
    var until: LocalDate,
) : MembershipEvent(number) {
    override fun applyTo(target: GymMembership) = target.also {
        if (it.number == null) {
            it.number = number
            it.member = member
            it.until = until
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateMembershipEvent

        if (member != other.member) return false
        if (until != other.until) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        var result = member.hashCode()
        result = 31 * result + until.hashCode()
        result = 31 * result + number.hashCode()
        return result
    }

    override fun toString() =
        "CreateMembershipEvent(number='$number', member='$member', until=$until)"
}