package ru.kkrukhmalev.fitnessCentre.service.turnstile

import ru.kkrukhmalev.fitnessCentre.events.repository.*

fun main(args: Array<String>) =
    TurnstileService(
        MongoEventsRepositories(
            EventsWriteMongoRepository(),
            EventsReadMongoRepository()
        )
    ).start(args.getOrNull(0)?.toInt() ?: 8081)