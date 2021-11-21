import java.time.Clock
import java.time.Duration
import java.time.Instant

class EventsStatisticImpl(private val clock: Clock) : EventsStatistic {
    private val statistics: MutableMap<String, MutableList<Instant>> = mutableMapOf()

    private val maxTime = Duration.ofHours(1)

    override fun incEvent(name: String) {
        if (!statistics.containsKey(name))
            statistics[name] = mutableListOf()
        statistics[name]?.add(clock.instant())
    }

    override fun getEventStatisticByName(name: String): Double =
        getEventStatistic(statistics[name], clock.instant())

    override fun getAllEventStatistic(): Map<String, Double> {
        val now = clock.instant()
        return statistics.entries.associate { it.key to getEventStatistic(it.value, now) }.filter { it.value > 0 }
    }

    override fun printStatistic() {
        getAllEventStatistic().forEach {
            println("${it.key.padEnd(statistics.keys.maxByOrNull { k -> k.length }!!.length)} : ${it.value}")
        }
    }

    private fun getEventStatistic(events: List<Instant>?, instant: Instant): Double =
        events?.count { instant - maxTime <= it && it <= instant }?.div(60.0) ?: 0.0
}