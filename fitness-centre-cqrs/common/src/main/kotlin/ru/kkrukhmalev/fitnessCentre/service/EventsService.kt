package ru.kkrukhmalev.fitnessCentre.service

import ru.kkrukhmalev.fitnessCentre.events.repository.EventsRepositories

abstract class EventsService(val eventsRepositories: EventsRepositories<*, *>) {
    abstract fun start(port: Int)

    open fun clearState() {}
}