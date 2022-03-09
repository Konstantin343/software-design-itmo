package ru.kkrukhmalev.fitnessCentre.model.membership

import java.time.LocalDate

class GymMembership {
    var number: Long? = null
    var member: String? = null
    var until: LocalDate? = null

    override fun toString() =
        "Number: " + number + "\n" +
                "Member: " + member + "\n" +
                "Until: " + until
}