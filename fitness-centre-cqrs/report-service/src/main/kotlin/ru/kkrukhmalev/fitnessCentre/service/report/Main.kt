package ru.kkrukhmalev.fitnessCentre.service.report

import ru.kkrukhmalev.fitnessCentre.events.repository.*

fun main(args: Array<String>) =
    ReportsService(
        MongoEventsRepositories(
            EventsWriteMongoRepository(),
            EventsReadMongoRepository()
        )
    ).start(args.getOrNull(0)?.toInt() ?: 8082)