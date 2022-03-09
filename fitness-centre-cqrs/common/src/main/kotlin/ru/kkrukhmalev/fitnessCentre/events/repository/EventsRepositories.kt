package ru.kkrukhmalev.fitnessCentre.events.repository

import ru.kkrukhmalev.fitnessCentre.events.Event

interface EventsReadRepository {
    fun getEvents(fromExclusive: Long = 0): List<Event<*>>
}

interface EventsWriteRepository {
    fun publishEvent(event: Event<*>)
}

abstract class EventsRepositories<WR : EventsWriteRepository, RR : EventsReadRepository>(
    val writeRepository: WR,
    val readRepository: RR
)