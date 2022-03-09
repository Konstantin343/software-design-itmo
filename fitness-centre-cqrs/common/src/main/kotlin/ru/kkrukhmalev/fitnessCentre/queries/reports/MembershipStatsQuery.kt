package ru.kkrukhmalev.fitnessCentre.queries.reports

import ru.kkrukhmalev.fitnessCentre.events.repository.EventsReadRepository
import ru.kkrukhmalev.fitnessCentre.events.turnstile.TurnstileEvent
import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visits
import ru.kkrukhmalev.fitnessCentre.queries.Query

class MembershipStatsQuery(
    private val fromExclusive: Long,
) : Query<Pair<Visits, Long>>() {
    override fun execute(readRepository: EventsReadRepository): Pair<Visits, Long> {
        val visits = Visits()
        val events = readRepository.getEvents(fromExclusive)
        events.filterIsInstance<TurnstileEvent>()
            .sortedBy { it.order!! }
            .forEach {
                it.applyTo(visits)
            }
        return visits to (events.maxOfOrNull { it.order!! } ?: 0)
    }
}