package ru.kkrukhmalev.fitnessCentre.events

abstract class Event<T> {
    var order: Long? = null
    
    abstract fun applyTo(target: T): T
}