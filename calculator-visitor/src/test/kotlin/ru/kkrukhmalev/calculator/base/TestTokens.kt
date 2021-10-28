package ru.kkrukhmalev.calculator.base

import ru.kkrukhmalev.calculator.tokens.*

@Suppress("DANGEROUS_CHARACTERS", "ObjectPropertyName", "unused")
internal object TestTokens {
    val `(` get() = OpenBrace()
    val `)` get() = CloseBrace()
    val `+` get() = SumOperation()
    val `-` get() = SubOperation()
    val `*` get() = MulOperation()
    val `|` get() = DivOperation()

    fun List<Any>.toTokens(): List<Any> = this.map { if (it is Int) NumberToken(it) else it }
}