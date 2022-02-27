package ru.kkrukhmalev.reactiveWebService.currency

interface CurrencyCalculator {
    fun calculate(value: Double, from: Currency, to: Currency): Double
}
