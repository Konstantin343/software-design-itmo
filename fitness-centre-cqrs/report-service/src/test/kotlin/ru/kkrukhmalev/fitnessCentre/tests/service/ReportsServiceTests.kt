package ru.kkrukhmalev.fitnessCentre.tests.service

import org.junit.jupiter.api.Test
import ru.kkrukhmalev.fitnessCentre.events.turnstile.EnterEvent
import ru.kkrukhmalev.fitnessCentre.events.turnstile.ExitEvent
import ru.kkrukhmalev.fitnessCentre.service.report.ReportsService
import ru.kkrukhmalev.fitnessCentre.tests.EventsServiceTests
import java.time.LocalDate
import java.time.LocalDateTime

class ReportsServiceTests : EventsServiceTests<ReportsService>({ ReportsService(it) }) {
    @Test
    fun reportStatsByNumber() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),
        
        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 1),
        
        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/stats",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse(
            """
            2022-01-01:
             Number: 1, Time: from 2022-01-01T10:00 to 2022-01-01T14:00
             Number: 1, Time: from 2022-01-01T14:30 to 2022-01-01T14:40
            2022-01-03:
             Number: 1, Time: from 2022-01-03T12:00 to 2022-01-03T18:00
            """.trimIndent()
        )
    }

    @Test
    fun dontReportStatsForOther() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 2),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 2),

        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/stats",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse(
            """
            2022-01-01:
             Number: 1, Time: from 2022-01-01T10:00 to 2022-01-01T14:00
            2022-01-03:
             Number: 1, Time: from 2022-01-03T12:00 to 2022-01-03T18:00
            """.trimIndent()
        )
    }

    @Test
    fun reportStatsForAllMembers() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 2),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 2),

        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/stats",
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse(
            """
            2022-01-01:
             Number: 1, Time: from 2022-01-01T10:00 to 2022-01-01T14:00
             Number: 2, Time: from 2022-01-01T14:30 to 2022-01-01T14:40
            2022-01-03:
             Number: 1, Time: from 2022-01-03T12:00 to 2022-01-03T18:00
            """.trimIndent()
        )
    }

    @Test
    fun dontReportStatsNotInRange() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 2, 14, 30), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 2, 14, 40), 1),

        EnterEvent(LocalDateTime.of(2022, 2, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 2, 3, 18, 0), 1),
    ) {
        request(
            "/reports/stats",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse(
            """
            2022-01-01:
             Number: 1, Time: from 2022-01-01T10:00 to 2022-01-01T14:00
            2022-01-02:
             Number: 1, Time: from 2022-01-02T14:30 to 2022-01-02T14:40
            """.trimIndent()
        )
    }

    @Test
    fun reportAverageByNumber() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/average",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse("PT3H23M20S")
    }

    @Test
    fun dontReportAverageForOther() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 2),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 2),

        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/average",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse("PT5H")
    }

    @Test
    fun reportAverageForAllMembers() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 1, 14, 30), 2),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 40), 2),

        EnterEvent(LocalDateTime.of(2022, 1, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 3, 18, 0), 1),
    ) {
        request(
            "/reports/average",
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse("PT3H23M20S")
    }

    @Test
    fun dontReportAverageNotInRange() = withEvents(
        EnterEvent(LocalDateTime.of(2022, 1, 1, 10, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 1, 14, 0), 1),

        EnterEvent(LocalDateTime.of(2022, 1, 2, 14, 30), 1),
        ExitEvent(LocalDateTime.of(2022, 1, 2, 14, 40), 1),

        EnterEvent(LocalDateTime.of(2022, 2, 3, 12, 0), 1),
        ExitEvent(LocalDateTime.of(2022, 2, 3, 18, 0), 1),
    ) {
        request(
            "/reports/average",
            "number" to 1,
            "from" to LocalDate.of(2022, 1, 1),
            "to" to LocalDate.of(2022, 1, 30)
        ).shouldResponse("PT2H5M")
    }
}