package ru.kkrukhmalev.stockMarket.account.tests

import io.ktor.http.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.ImageFromDockerfile
import ru.kkrukhmalev.stockMarket.account.PersonalAccount
import ru.kkrukhmalev.stockMarket.http.request
import ru.kkrukhmalev.stockMarket.repository.MongoStockRepository
import ru.kkrukhmalev.stockMarket.repository.StockRepository
import java.nio.file.Path
import java.time.Duration
import kotlin.math.abs

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Tests {
    companion object {
        const val STOCK_MARKET_PORT = 9090
        const val DB_PORT = 9091

        const val MONGO_DOCKER_IMAGE = "mongo:4.0.10"

        val timeout: Duration = Duration.ofMinutes(5)
    }

    private val mongoDbContainer = MongoDBContainer(MONGO_DOCKER_IMAGE).apply {
        portBindings = listOf("$DB_PORT:27017")
    }

    private operator fun Path.div(other: String) = this.resolve(other)

    private operator fun String.div(other: String) = Path.of(this).resolve(other)

    private val image = ImageFromDockerfile().withDockerfile(".." / "Dockerfile")
    private val stockMarketContainer = GenericContainer(image)
        .withEnv("PORT", "$STOCK_MARKET_PORT")
        .withEnv("DB_CONNECTION", "mongodb://host.docker.internal:$DB_PORT")
        .waitingFor(Wait
            .forHttp("/companies/all")
            .forStatusCode(HttpStatusCode.OK.value)
            .forPort(STOCK_MARKET_PORT)
        )
        .withStartupTimeout(timeout)
        .apply {
            setPortBindings(listOf("$STOCK_MARKET_PORT:$STOCK_MARKET_PORT"))
        }

    private lateinit var personalAccount: PersonalAccount
    
    private lateinit var stockRepository: StockRepository

    @BeforeAll
    fun startTestContainers() {
        mongoDbContainer.start()
        stockMarketContainer.start()

        stockRepository = MongoStockRepository("mongodb://localhost:$DB_PORT")        
        personalAccount = PersonalAccount(stockRepository, "localhost", STOCK_MARKET_PORT)
    }
    
    @AfterEach
    fun clearDatabase() {
        stockRepository.clearAll()
    }

    object StockMarketTestApi {
        private fun request(uri: String, vararg parameters: Pair<String, Any>) =
            request("localhost:$STOCK_MARKET_PORT", uri, *parameters)

        fun addCompany(name: String, startPrice: Double) = request(
            "/companies/add",
            "company" to name,
            "startPrice" to startPrice
        )

        fun getCompany(name: String) = request(
            "/companies/get",
            "company" to name
        )

        fun addStocks(name: String, count: Int) = request(
            "/stocks/add",
            "company" to name,
            "count" to count
        )
    }
    
    private fun parsePrice(companyString: String) = ".*price=(.+?)\\).*".toRegex()
        .matchEntire(companyString)!!
        .groupValues[1].toDouble()

    @Test
    fun buyStocks() {
        val companyName = "Company"
        val login = "user"
        val name = "User"

        StockMarketTestApi.addCompany(companyName, 100.0)
        StockMarketTestApi.addStocks(companyName, 100)
        personalAccount.registerUser(login, name)
        personalAccount.topUp(login, 50000.0)

        personalAccount.buyStocks(login, companyName, 10)

        val stocks = personalAccount.getAllStocks(login)
        val stock = stocks.entries.single().toPair()
        val price = parsePrice(StockMarketTestApi.getCompany(companyName).body())
        Assertions.assertEquals(companyName to (10 to price), stock)
    }
    
    @Test
    fun cantBuyMoreStocksThanExists() {
        val companyName = "Company"
        val login = "user"
        val name = "User"

        StockMarketTestApi.addCompany(companyName, 100.0)
        StockMarketTestApi.addStocks(companyName, 10)
        personalAccount.registerUser(login, name)
        personalAccount.topUp(login, 50000.0)

        Assertions.assertThrows(Exception::class.java, {
            personalAccount.buyStocks(login, companyName, 20)
        }, "Status 400: No stocks in market")
        Assertions.assertTrue(personalAccount.getAllStocks(login).isEmpty())
    }

    @Test
    fun cantBuyMoreStocksThanMoney() {
        val companyName = "Company"
        val login = "user"
        val name = "User"

        StockMarketTestApi.addCompany(companyName, 100.0)
        StockMarketTestApi.addStocks(companyName, 10)
        personalAccount.registerUser(login, name)

        Assertions.assertThrows(Exception::class.java, {
            personalAccount.buyStocks(login, companyName, 5)
        }, "Status 400: Not enough money")
        Assertions.assertTrue(personalAccount.getAllStocks(login).isEmpty())
    }

    @Test
    fun sellStocksWithOtherPrice() {
        val companyName = "Company"
        val login = "user"
        val name = "User"

        StockMarketTestApi.addCompany(companyName, 100.0)
        StockMarketTestApi.addStocks(companyName, 100)
        personalAccount.registerUser(login, name)
        personalAccount.topUp(login, 50000.0)

        val price1 = parsePrice(StockMarketTestApi.getCompany(companyName).body())
        personalAccount.buyStocks(login, companyName, 10)
        
        val price2 = parsePrice(StockMarketTestApi.getCompany(companyName).body())
        
        personalAccount.sellStocks(login, companyName, 10)
        val sumMoney = personalAccount.getSumMoney(login)
        Assertions.assertTrue(abs(50000.0 + (price2 - price1) * 10 - sumMoney) < 0.01)
    }

    @Test
    fun sumMoneyFromTwoCompanies() {
        val companyName1 = "Company1"
        val companyName2 = "Company2"
        val login = "user"
        val name = "User"

        StockMarketTestApi.addCompany(companyName1, 100.0)
        StockMarketTestApi.addStocks(companyName1, 100)
        StockMarketTestApi.addCompany("Company2", 100.0)
        StockMarketTestApi.addStocks("Company2", 100)
        personalAccount.registerUser(login, name)
        personalAccount.topUp(login, 50000.0)

        val price11 = parsePrice(StockMarketTestApi.getCompany(companyName1).body())
        val price21 = parsePrice(StockMarketTestApi.getCompany(companyName2).body())

        personalAccount.buyStocks(login, companyName1, 10)
        personalAccount.buyStocks(login, companyName2, 10)

        val price12 = parsePrice(StockMarketTestApi.getCompany(companyName1).body())
        val price22 = parsePrice(StockMarketTestApi.getCompany(companyName2).body())
        
        val sumMoney = personalAccount.getSumMoney(login)
        Assertions.assertTrue(
            abs(50000.0 + (price12 - price11) * 10.0 + (price22 - price21) * 10.0 - sumMoney) < 0.01
        )
    }
}