package ru.kkrukhmalev.fitnessCentre.queries

import ru.kkrukhmalev.fitnessCentre.events.repository.EventsReadRepository

abstract class Query<T> {
    abstract fun execute(readRepository: EventsReadRepository): T
}