package ru.kkrukhmalev.fitnessCentre.events.repository

import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import org.bson.Document
import ru.kkrukhmalev.fitnessCentre.events.Event
import ru.kkrukhmalev.fitnessCentre.events.membership.CreateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.membership.UpdateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.turnstile.EnterEvent
import ru.kkrukhmalev.fitnessCentre.events.turnstile.ExitEvent
import java.time.LocalDate
import java.time.LocalDateTime

fun Document.toEvent(): Event<*> =
    when (this["eventType"]) {
        "Enter" -> EnterEvent(LocalDateTime.parse(this["time"].toString()), this["number"].toString().toLong())
        "Exit" -> ExitEvent(LocalDateTime.parse(this["time"].toString()), this["number"].toString().toLong())
        "CreateMembership" -> CreateMembershipEvent(
            this["number"].toString().toLong(),
            this["member"].toString(),
            LocalDate.parse(this["until"].toString())
        )
        "UpdateMembership" -> UpdateMembershipEvent(
            this["number"].toString().toLong(),
            LocalDate.parse(this["until"].toString())
        )
        else -> throw IllegalArgumentException("Unexpected eventType: ${this["eventType"]}")
    }.also {
        it.order = this["order"].toString().toLong()
    }

fun Event<*>.toDocument(): Document =
    when (this) {
        is EnterEvent -> Document(mapOf("eventType" to "Enter",
            "time" to this.time.toString(),
            "number" to this.memberNumber))
        is ExitEvent -> Document(mapOf("eventType" to "Exit",
            "time" to this.time.toString(),
            "number" to this.memberNumber))
        is CreateMembershipEvent -> Document(mapOf("eventType" to "CreateMembership",
            "until" to this.until.toString(),
            "number" to this.number,
            "member" to this.member))
        is UpdateMembershipEvent -> Document(mapOf("eventType" to "UpdateMembership",
            "until" to this.until.toString(),
            "number" to this.number))
        else -> throw IllegalArgumentException("Unexpected Event<*>: ${this.javaClass.name}")
    }

class EventsReadMongoRepository : EventsReadRepository {
    private val client = MongoClients.create("mongodb://localhost:27017")

    override fun getEvents(fromExclusive: Long): List<Event<*>> {
        val documents = client.getDatabase("events")
            .getCollection("events")
            .find(Filters.gt("order", fromExclusive))
            .toList()
        return documents.map { it.toEvent() }
    }
}

class EventsWriteMongoRepository : EventsWriteRepository {
    private val client = MongoClients.create("mongodb://localhost:27017")

    override fun publishEvent(event: Event<*>) {
        val events = client.getDatabase("events").getCollection("events")
        events.insertOne(event.toDocument()
            .apply {
                this["order"] = (events.find().toList().maxOfOrNull { it["order"].toString().toLong() } ?: 0) + 1
            })
    }
}

class MongoEventsRepositories(
    writeMongoRepository: EventsWriteMongoRepository,
    readMongoRepository: EventsReadMongoRepository,
) : EventsRepositories<EventsWriteMongoRepository, EventsReadMongoRepository>(
    writeMongoRepository, readMongoRepository
)
