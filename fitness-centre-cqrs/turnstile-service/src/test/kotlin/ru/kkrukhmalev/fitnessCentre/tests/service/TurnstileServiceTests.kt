package ru.kkrukhmalev.fitnessCentre.tests.service

import org.junit.jupiter.api.Test
import ru.kkrukhmalev.fitnessCentre.events.membership.CreateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.membership.UpdateMembershipEvent
import ru.kkrukhmalev.fitnessCentre.events.turnstile.EnterEvent
import ru.kkrukhmalev.fitnessCentre.events.turnstile.ExitEvent
import ru.kkrukhmalev.fitnessCentre.service.turnstile.TurnstileService
import ru.kkrukhmalev.fitnessCentre.tests.EventsServiceTests
import java.time.*

class TurnstileServiceTests : EventsServiceTests<TurnstileService>({
    TurnstileService(it).apply {
        val zone = ZoneOffset.ofTotalSeconds(0)
        val fixedTime = LocalDateTime.of(2022, 1, 1, 10, 0)
        setClock(Clock.fixed(Instant.ofEpochSecond(fixedTime.toEpochSecond(zone)), zone))
    }
}) {
    @Test
    fun enterForExistingMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1))
    ) {
        request(
            "/turnstile/enter",
            "number" to 1
        ).shouldResponse("Result: Success")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1)),
            EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1)
        )
    }

    @Test
    fun dontEnterForOtherMembership() = withEvents(
        CreateMembershipEvent(2, "Member", LocalDate.of(2023, 1, 1))
    ) {
        request(
            "/turnstile/enter",
            "number" to 1
        ).shouldResponse("Result: No Membership")

        eventsShouldBe(
            CreateMembershipEvent(2, "Member", LocalDate.of(2023, 1, 1))
        )
    }

    @Test
    fun enterForUpdatedMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1)),
        UpdateMembershipEvent(1, LocalDate.of(2023, 1, 1))
    ) {
        request(
            "/turnstile/enter",
            "number" to 1
        ).shouldResponse("Result: Success")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1)),
            UpdateMembershipEvent(1, LocalDate.of(2023, 1, 1)),
            EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1)
        )
    }

    @Test
    fun dontEnterForOldMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1))
    ) {
        request(
            "/turnstile/enter",
            "number" to 1
        ).shouldResponse("Result: No Membership")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2021, 1, 1))
        )
    }

    @Test
    fun exitForExistingMembership() = withEvents(
        CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1)),
        EnterEvent(LocalDateTime.of(2022, 1, 1, 9, 0), 1)
    ) {
        request(
            "/turnstile/exit",
            "number" to 1
        ).shouldResponse("Result: Success")

        eventsShouldBe(
            CreateMembershipEvent(1, "Member", LocalDate.of(2023, 1, 1)),
            EnterEvent(LocalDateTime.of(2022, 1, 1, 9, 0), 1),
            ExitEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1)
        )
    }

    @Test
    fun dontExitForOtherMembership() = withEvents(
        CreateMembershipEvent(2, "Member", LocalDate.of(2023, 1, 1)),
        EnterEvent(LocalDateTime.of(2022, 1, 1, 9, 0), 1)
    ) {
        request(
            "/turnstile/exit",
            "number" to 1
        ).shouldResponse("Result: No Membership")

        eventsShouldBe(
            CreateMembershipEvent(2, "Member", LocalDate.of(2023, 1, 1)),
            EnterEvent(LocalDateTime.of(2022, 1, 1, 9, 0), 1)
        )
    }
}