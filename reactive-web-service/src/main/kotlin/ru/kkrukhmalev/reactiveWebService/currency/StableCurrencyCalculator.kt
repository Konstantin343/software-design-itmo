package ru.kkrukhmalev.reactiveWebService.currency

import kotlin.math.roundToInt

class StableCurrencyCalculator : CurrencyCalculator {
    private val coursesInRub: Map<Currency, Double> = mapOf(
        Currency.RUB to 1.0,
        Currency.USD to 80.0,
        Currency.EUR to 90.0
    )

    override fun calculate(value: Double, from: Currency, to: Currency) =
        (value * coursesInRub[from]!! / coursesInRub[to]!! * 100.0).roundToInt().toDouble() / 100.0
}