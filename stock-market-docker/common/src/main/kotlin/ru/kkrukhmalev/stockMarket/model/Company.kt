package ru.kkrukhmalev.stockMarket.model

data class Company(
    var name: String,
    var stocksCount: Int,
    var price: Double
)