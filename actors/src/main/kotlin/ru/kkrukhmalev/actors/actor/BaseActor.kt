package ru.kkrukhmalev.actors.actor

import akka.actor.UntypedActor
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseActor : UntypedActor() {
    protected var childCount = AtomicInteger(0)

    override fun postStop() {
        super.postStop()
        println("Actor '${self.path().name()}' stopped")
    }

    override fun preStart() {
        super.preStart()
        println("Actor '${self.path().name()}' started")
    }
}
