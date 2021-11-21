import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant

class EventsStatisticTest {

    private lateinit var eventsStatistic: EventsStatistic

    private lateinit var clock: SettableClock

    @BeforeEach
    fun createInstance() {
        clock = SettableClock(Clock.fixed(Instant.now(), Clock.systemUTC().zone))
        eventsStatistic = EventsStatisticImpl(clock)
    }

    @Test
    fun testOneEvent() {
        eventsStatistic.incEvent("event1")
        Assertions.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("event1"))
        Assertions.assertEquals(
            mapOf(
                "event1" to 1 / 60.0
            ),
            eventsStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun testOldEvent() {
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(40))
        eventsStatistic.incEvent("event2")
        clock.move(Duration.ofMinutes(40))
        eventsStatistic.incEvent("event3")
        Assertions.assertEquals(
            mapOf(
                "event2" to 1 / 60.0,
                "event3" to 1 / 60.0,
            ),
            eventsStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun testManySameEvents() {
        for (i in 1..120) {
            eventsStatistic.incEvent("event")
            clock.move(Duration.ofSeconds(20))
        }
        
        Assertions.assertEquals(
            mapOf(
                "event" to 2.0
            ),
            eventsStatistic.getAllEventStatistic()
        )
    }
    
    @Test
    fun testEventNotExist() {
        Assertions.assertEquals(0.0, eventsStatistic.getEventStatisticByName("event"))
    }
    
    @Test
    fun testEventOnBounds() {
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(60))
        Assertions.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("event1"))
        
        clock.move(Duration.ofNanos(1))
        Assertions.assertEquals(0.0, eventsStatistic.getEventStatisticByName("event1"))
    }

    @Test
    fun testManyDifferentEvents() {
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(10))
        eventsStatistic.incEvent("event2")
        clock.move(Duration.ofMinutes(5))
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(20))
        eventsStatistic.incEvent("event2")
        clock.move(Duration.ofMinutes(2))
        eventsStatistic.incEvent("event3")
        clock.move(Duration.ofMinutes(1))
        eventsStatistic.incEvent("event3")
        clock.move(Duration.ofMinutes(5))
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(10))
        eventsStatistic.incEvent("event4")
        clock.move(Duration.ofMinutes(20))
        eventsStatistic.incEvent("event1")
        clock.move(Duration.ofMinutes(1))
        eventsStatistic.incEvent("event4")
        clock.move(Duration.ofMinutes(15))
        eventsStatistic.incEvent("event2")
        clock.move(Duration.ofMinutes(11))
        eventsStatistic.incEvent("event3")
        
        Assertions.assertEquals(
            mapOf(
                "event1" to 1 / 30.0,
                "event2" to 1 / 60.0,
                "event3" to 1 / 60.0,
                "event4" to 1 / 30.0,
            ),
            eventsStatistic.getAllEventStatistic()
        )
    }
}
