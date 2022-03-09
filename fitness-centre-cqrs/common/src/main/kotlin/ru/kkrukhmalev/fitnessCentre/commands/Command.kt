package ru.kkrukhmalev.fitnessCentre.commands

import ru.kkrukhmalev.fitnessCentre.events.repository.EventsWriteRepository

abstract class Command {
    abstract fun execute(writeRepository: EventsWriteRepository)
}