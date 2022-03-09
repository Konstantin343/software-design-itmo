package ru.kkrukhmalev.fitnessCentre.queries.membership

import ru.kkrukhmalev.fitnessCentre.events.membership.MembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsReadRepository
import ru.kkrukhmalev.fitnessCentre.model.membership.GymMembership
import ru.kkrukhmalev.fitnessCentre.queries.Query

class GetMembershipQuery(private val number: Long) : Query<GymMembership>() {
    override fun execute(readRepository: EventsReadRepository): GymMembership {
        val gymMembership = GymMembership()
        readRepository.getEvents(0)
            .filterIsInstance<MembershipEvent>()
            .sortedBy { it.order!! }
            .forEach {
                if (it.number == number) {
                    it.applyTo(gymMembership)
                }
            }
        return gymMembership
    }
}