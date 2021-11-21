import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class SettableClock(init: Clock) : Clock() {
    private var clock: Clock = init

    override fun getZone(): ZoneId = clock.zone

    override fun withZone(zone: ZoneId): Clock = clock.withZone(getZone())

    override fun instant(): Instant = clock.instant()
    
    fun set(clock: Clock) {
        this.clock = clock
    }
    
    fun move(duration: Duration) = set(offset(clock, duration))
}
