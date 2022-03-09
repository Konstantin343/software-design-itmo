package ru.kkrukhmalev.fitnessCentre.events.repository

import ru.kkrukhmalev.fitnessCentre.events.Event

private val events: MutableList<Event<*>> = mutableListOf()

class EventsReadInMemoryRepository : EventsReadRepository {
    override fun getEvents(fromExclusive: Long): List<Event<*>> {
        return events.filter { it.order!! > fromExclusive }.sortedBy { it.order }
    }
}

class EventsWriteInMemoryRepository : EventsWriteRepository {
    override fun publishEvent(event: Event<*>) {
        events.add(event.apply {
            order = (events.maxOfOrNull { it.order!! } ?: 0) + 1
        })
    }
}

class InMemoryEventsRepositories(
    writeInMemoryRepository: EventsWriteInMemoryRepository,
    readInMemoryRepository: EventsReadInMemoryRepository
) : EventsRepositories<EventsWriteInMemoryRepository, EventsReadInMemoryRepository>(
    writeInMemoryRepository, readInMemoryRepository
) {
    fun clearAll() = events.clear()
}