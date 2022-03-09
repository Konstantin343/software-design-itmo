package ru.kkrukhmalev.fitnessCentre.service.report

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.kkrukhmalev.fitnessCentre.events.repository.EventsRepositories
import ru.kkrukhmalev.fitnessCentre.model.turnstile.Visits
import ru.kkrukhmalev.fitnessCentre.queries.reports.MembershipStatsQuery
import ru.kkrukhmalev.fitnessCentre.service.EventsService
import java.time.LocalDate

class ReportsService(eventsRepositories: EventsRepositories<*, *>) : EventsService(eventsRepositories) {
    private var lastEvent: Long = 0

    private val visits = Visits()
    
    override fun start(port: Int) {
        updateVisits()
        embeddedServer(Netty, port) {
            routing {
                get("/reports/stats") {
                    val parameters = this.context.request.queryParameters
                    updateVisits()
                    call.respondText {
                        visits.visitsStats(
                            LocalDate.parse(parameters["from"]!!),
                            LocalDate.parse(parameters["to"]!!),
                            parameters["number"]?.toLong()
                        ).entries
                            .joinToString("\n") {
                                "${it.key}:\n " + it.value.joinToString("\n ") { v ->
                                    "Number: " + v.number + ", Time: from " + v.start + " to " + v.end
                                }
                            }
                    }
                }
                get("/reports/average") {
                    val parameters = this.context.request.queryParameters
                    updateVisits()
                    call.respondText {
                        visits.averageVisitTime(
                            LocalDate.parse(parameters["from"]!!),
                            LocalDate.parse(parameters["to"]!!),
                            parameters["number"]?.toLong()
                        ).toString()
                    }
                }
            }
        }.start(wait = true)
    }

    private fun updateVisits() {
        val (stats, last) = MembershipStatsQuery(lastEvent).execute(eventsRepositories.readRepository)

        lastEvent = last
        visits.visits.addAll(stats.visits)
    }

    override fun clearState() {
        visits.visits.clear()
        lastEvent = 0
    }
}