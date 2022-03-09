package ru.kkrukhmalev.fitnessCentre.commands.turnstile

import ru.kkrukhmalev.fitnessCentre.commands.Command
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsWriteRepository
import ru.kkrukhmalev.fitnessCentre.events.turnstile.ExitEvent
import java.time.LocalDateTime

class ExitCommand(
    private val time: LocalDateTime,
    private val number: Long,
) : Command() {
    override fun execute(writeRepository: EventsWriteRepository) {
        writeRepository.publishEvent(ExitEvent(time, number))
    }
}