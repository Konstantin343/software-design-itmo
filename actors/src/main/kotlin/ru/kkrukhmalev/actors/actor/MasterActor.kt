package ru.kkrukhmalev.actors.actor

import akka.actor.ActorRef
import akka.actor.Props
import ru.kkrukhmalev.actors.api.SearchApi
import ru.kkrukhmalev.actors.model.SearchRequest
import ru.kkrukhmalev.actors.model.SearchResult
import ru.kkrukhmalev.actors.model.SearchResultsMessage
import java.time.Duration
import kotlin.concurrent.thread

class MasterActor(
    private val searchApis: List<SearchApi>,
    private val countBySearchApi: Int,
    private val timeout: Duration,
) : BaseActor() {
    private val resultReceived: MutableMap<ActorRef, Boolean> = mutableMapOf()

    private var searchStarted = false

    private val result: MutableList<SearchResult> = mutableListOf()

    private lateinit var initiator: ActorRef

    override fun onReceive(message: Any) {
        when (message) {
            is SearchRequest -> {
                if (searchStarted)
                    return
                searchStarted = true
                initiator = sender


                for (searchApi in searchApis) {
                    val child = context.actorOf(
                        Props.create(ChildActor::class.java, searchApi, countBySearchApi),
                        "${self.path().name()}_child${childCount.getAndIncrement()}_" + searchApi.name
                    )
                    resultReceived[child] = false
                }
                for (actorRef in resultReceived.keys) {
                    actorRef.tell(message, self)
                }

                setTimeout()
            }
            is SearchResultsMessage -> {
                result.addAll(message.results)
                resultReceived[sender] = true
                if (resultReceived.values.all { it }) {
                    finish()
                }
            }
        }
    }

    private fun setTimeout() {
        thread {
            val endTime = System.currentTimeMillis() + timeout.toMillis()
            while (!self.isTerminated && System.currentTimeMillis() < endTime) {
                Thread.sleep(100)
            }
            finish()
        }
    }

    private fun finish() {
        if (self.isTerminated)
            return
        initiator.tell(SearchResultsMessage(result), self)
        context.stop(self)
    }
}