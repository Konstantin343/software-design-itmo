package ru.kkrukhmalev.fitnessCentre.service.manager

import ru.kkrukhmalev.fitnessCentre.events.repository.*

fun main(args: Array<String>) =
    ManagerService(
        MongoEventsRepositories(
            EventsWriteMongoRepository(),
            EventsReadMongoRepository()
        )
    ).start(args.getOrNull(0)?.toInt() ?: 8080)