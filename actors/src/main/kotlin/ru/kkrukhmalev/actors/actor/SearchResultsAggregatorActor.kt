package ru.kkrukhmalev.actors.actor

import akka.actor.ActorRef
import akka.actor.Props
import ru.kkrukhmalev.actors.api.SearchApi
import ru.kkrukhmalev.actors.model.SearchRequest
import ru.kkrukhmalev.actors.model.SearchResultsMessage

class SearchResultsAggregatorActor(
    private val searchApis: List<SearchApi>
) : BaseActor() {
    private val messagesToResults = mutableMapOf<ActorRef, SearchRequest>()

    override fun onReceive(message: Any) {
        when (message) {
            is SearchRequest -> {
                val master: ActorRef = context.actorOf(
                    Props.create(MasterActor::class.java, searchApis, message.count, message.timeout),
                    "master${childCount.getAndIncrement()}"
                )
                messagesToResults[master] = message
                master.tell(message, this.self)
            }
            is SearchResultsMessage -> {
                messagesToResults[sender]?.result?.addAll(message.results)
                messagesToResults[sender]?.complete = true
                messagesToResults.remove(sender)
            }
        }
    }
}