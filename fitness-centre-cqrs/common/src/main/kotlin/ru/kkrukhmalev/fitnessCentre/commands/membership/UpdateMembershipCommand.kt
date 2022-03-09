package ru.kkrukhmalev.fitnessCentre.commands.membership

import ru.kkrukhmalev.fitnessCentre.commands.Command
import ru.kkrukhmalev.fitnessCentre.events.membership.UpdateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsWriteRepository
import java.time.LocalDate

class UpdateMembershipCommand(
    var number: Long,
    var until: LocalDate,
) : Command() {
    override fun execute(writeRepository: EventsWriteRepository) {
        writeRepository.publishEvent(UpdateMembershipEvent(number, until))
    }
}