package ru.kkrukhmalev.actors.tests

import com.xebialabs.restito.server.StubServer
import org.junit.jupiter.api.*
import ru.kkrukhmalev.actors.SearchResultsAggregator
import ru.kkrukhmalev.actors.api.BingSearchApi
import ru.kkrukhmalev.actors.api.GoogleSearchApi
import ru.kkrukhmalev.actors.api.YandexSearchApi
import ru.kkrukhmalev.actors.stubServer.prepareHttpResponse
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTestWithStubServer(private val port: Int = 8081) {
    private val searchApis = listOf(
        YandexSearchApi("localhost:$port"),
        GoogleSearchApi("localhost:$port"),
        BingSearchApi("localhost:$port")
    )

    private val searchResultsAggregator = SearchResultsAggregator(searchApis)

    lateinit var stubServer: StubServer

    fun request(query: String, resultsCount: Int, timeout: Duration = Duration.ofSeconds(30)) =
        searchResultsAggregator.request(query, resultsCount, timeout)

    @BeforeEach
    fun startStubServer(testInfo: TestInfo) {
        stubServer = StubServer(port)
        stubServer.start()
        val rules = testInfo.testMethod.get().getAnnotation(StubServerRules::class.java).rules
        for (rule in rules) {
            stubServer.prepareHttpResponse(rule.browser, rule.resultsCount, rule.statusCode, rule.delay)
        }
    }

    @AfterEach
    fun stopStubServer() {
        stubServer.stop()
    }
    
    @AfterAll
    fun stopActorSystem() {
        searchResultsAggregator.stop()
    }
}