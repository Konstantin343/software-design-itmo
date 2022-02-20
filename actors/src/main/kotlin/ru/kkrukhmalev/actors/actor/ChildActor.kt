package ru.kkrukhmalev.actors.actor

import ru.kkrukhmalev.actors.api.SearchApi
import ru.kkrukhmalev.actors.model.SearchRequest
import ru.kkrukhmalev.actors.model.SearchResultsMessage

class ChildActor(private val searchApi: SearchApi, private val count: Int) : BaseActor() {
    override fun onReceive(message: Any) {
        when (message) {
            is SearchRequest -> {
                try {
                    val results = searchApi.topNResults(message.text, count)
                    sender.tell(SearchResultsMessage(results), self)
                } catch (e: Throwable) {
                    context.system().log().warning(e.message)
                    sender.tell(SearchResultsMessage(listOf()), self)
                } finally {
                    context.stop(self)
                }
            }
        }
    }
}