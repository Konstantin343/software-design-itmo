package ru.kkrukhmalev.fitnessCentre.events.membership

import ru.kkrukhmalev.fitnessCentre.events.Event
import ru.kkrukhmalev.fitnessCentre.model.membership.GymMembership

abstract class MembershipEvent(var number: Long) : Event<GymMembership>()