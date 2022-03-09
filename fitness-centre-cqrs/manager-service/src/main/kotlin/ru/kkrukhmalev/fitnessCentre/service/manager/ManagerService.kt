package ru.kkrukhmalev.fitnessCentre.service.manager

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.kkrukhmalev.fitnessCentre.commands.membership.CreateMembershipCommand
import ru.kkrukhmalev.fitnessCentre.commands.membership.UpdateMembershipCommand
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsRepositories
import ru.kkrukhmalev.fitnessCentre.queries.membership.GetMembershipQuery
import ru.kkrukhmalev.fitnessCentre.service.EventsService
import java.time.LocalDate

class ManagerService(eventsRepositories: EventsRepositories<*, *>) : EventsService(eventsRepositories) {
    override fun start(port: Int) {
        embeddedServer(Netty, port) {
            routing {
                get("/memberships/get") {
                    val parameters = this.context.request.queryParameters
                    call.respondText {
                        GetMembershipQuery(
                            parameters["number"]!!.toLong()
                        ).execute(eventsRepositories.readRepository).toString()
                    }
                }
                get("/memberships/create") {
                    val parameters = this.context.request.queryParameters
                    try {
                        CreateMembershipCommand(
                            parameters["number"]!!.toLong(),
                            parameters["member"]!!,
                            LocalDate.parse(parameters["until"]!!)
                        ).execute(eventsRepositories.writeRepository)
                    } catch (e: Exception) {
                        call.respondText { e.localizedMessage }
                    }
                }
                get("/memberships/update") {
                    val parameters = this.context.request.queryParameters
                    try {
                        UpdateMembershipCommand(
                            parameters["number"]!!.toLong(),
                            LocalDate.parse(parameters["until"]!!)
                        ).execute(eventsRepositories.writeRepository)
                    } catch (e: Exception) {
                        call.respondText { e.localizedMessage }
                    }
                }
            }
        }.start(wait = true)
    }
}