package ru.kkrukhmalev.fitnessCentre.tests.service

import org.junit.jupiter.api.Test
import ru.kkrukhmalev.fitnessCentre.events.membership.CreateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.membership.UpdateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.service.manager.ManagerService
import ru.kkrukhmalev.fitnessCentre.tests.EventsServiceTests
import java.time.LocalDate

class ManagerServiceTests : EventsServiceTests<ManagerService>({ ManagerService(it) }) {
    @Test
    fun createMembership() = withEvents {
        request(
            "/memberships/create",
            "number" to 1,
            "member" to "Member",
            "until" to LocalDate.of(2023, 1, 1)
        ).shouldResponse("")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1))
        )
    }

    @Test
    fun updateMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1))
    ) {
        request(
            "/memberships/update",
            "number" to 1,
            "until" to LocalDate.of(2023, 1, 1)
        ).shouldResponse("")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1)),
            UpdateMembershipEvent(1, LocalDate.of(2023, 1, 1))
        )
    }

    @Test
    fun getCreatedMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1))
    ) {
        request(
            "/memberships/get",
            "number" to 1
        ).shouldResponse("""
            Number: 1
            Member: Member
            Until: 2023-01-01
        """.trimIndent())
    }

    @Test
    fun getUpdateMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1)),
        UpdateMembershipEvent(1, LocalDate.of(2023, 1, 1))
    ) {
        request(
            "/memberships/get",
            "number" to 1
        ).shouldResponse("""
            Number: 1
            Member: Member
            Until: 2023-01-01
        """.trimIndent())
    }

    @Test
    fun dontGetOtherMembership() = withEvents(
        CreateMembershipEvent(2, "Member", LocalDate.of(2021, 1, 1))
    ) {
        request(
            "/memberships/get",
            "number" to 1
        ).shouldResponse("""
            Number: null
            Member: null
            Until: null
        """.trimIndent())
    }
}