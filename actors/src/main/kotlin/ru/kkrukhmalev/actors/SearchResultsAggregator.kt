package ru.kkrukhmalev.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import ru.kkrukhmalev.actors.actor.SearchResultsAggregatorActor
import ru.kkrukhmalev.actors.api.SearchApi
import ru.kkrukhmalev.actors.model.SearchRequest

class SearchResultsAggregator(searchApis: List<SearchApi>) {
    private val actor: ActorRef
    private val system: ActorSystem = ActorSystem.create("SearchAggregationSystem")

    init {
        actor = system.actorOf(
            Props.create(SearchResultsAggregatorActor::class.java, searchApis),
            "aggregator"
        )
    }

    fun aggregate(request: SearchRequest) {
        actor.tell(request, ActorRef.noSender())
    }
    
    fun stop() = system.shutdown()
}