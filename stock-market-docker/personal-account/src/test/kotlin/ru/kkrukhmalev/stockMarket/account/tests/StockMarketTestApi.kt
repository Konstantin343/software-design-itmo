package ru.kkrukhmalev.stockMarket.account.tests
import ru.kkrukhmalev.stockMarket.account.tests.PersonalAccountTests.Companion.STOCK_MARKET_PORT
import ru.kkrukhmalev.stockMarket.http.request

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