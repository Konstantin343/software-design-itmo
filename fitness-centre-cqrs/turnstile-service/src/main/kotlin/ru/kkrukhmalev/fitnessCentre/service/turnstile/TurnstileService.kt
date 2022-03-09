package ru.kkrukhmalev.fitnessCentre.service.turnstile

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.kkrukhmalev.fitnessCentre.commands.turnstile.EnterCommand
import ru.kkrukhmalev.fitnessCentre.commands.turnstile.ExitCommand
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsRepositories
import ru.kkrukhmalev.fitnessCentre.queries.membership.GetMembershipQuery
import ru.kkrukhmalev.fitnessCentre.service.EventsService
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

class TurnstileService(eventsRepositories: EventsRepositories<*, *>) : EventsService(eventsRepositories) {
    private var clock: Clock = Clock.systemUTC()
    
    override fun start(port: Int) {
        embeddedServer(Netty, port) {
            routing {
                get("/turnstile/enter") {
                    val parameters = this.context.request.queryParameters
                    call.respondText {
                        val membership = GetMembershipQuery(
                            parameters["number"]!!.toLong()
                        ).execute(eventsRepositories.readRepository)

                        if (membership.number != null && LocalDate.now(clock) <= membership.until!!) {
                            EnterCommand(LocalDateTime.now(clock).withNano(0),
                                membership.number!!).execute(eventsRepositories.writeRepository)
                            "Result: Success"
                        } else "Result: No Membership"
                    }
                }
                get("/turnstile/exit") {
                    val parameters = this.context.request.queryParameters
                    call.respondText {
                        val membership = GetMembershipQuery(
                            parameters["number"]!!.toLong()
                        ).execute(eventsRepositories.readRepository)

                        if (membership.number != null ) {
                            ExitCommand(LocalDateTime.now(clock).withNano(0),
                                membership.number!!).execute(eventsRepositories.writeRepository)
                            "Result: Success"
                        } else "Result: No Membership"
                    }
                }
            }
        }.start(wait = true)
    }
    
    fun setClock(clock: Clock) {
        this.clock = clock
    }
}