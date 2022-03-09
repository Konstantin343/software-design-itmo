package ru.kkrukhmalev.fitnessCentre.commands.membership

import ru.kkrukhmalev.fitnessCentre.commands.Command
import ru.kkrukhmalev.fitnessCentre.events.membership.CreateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsWriteRepository
import java.time.LocalDate

class CreateMembershipCommand(
    private val number: Long,
    private val member: String,
    private val until: LocalDate,
) : Command() {
    override fun execute(writeRepository: EventsWriteRepository) {
        writeRepository.publishEvent(CreateMembershipEvent(number, member, until))
    }
}