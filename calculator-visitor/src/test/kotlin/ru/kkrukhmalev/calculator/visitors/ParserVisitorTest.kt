package ru.kkrukhmalev.calculator.visitors

import org.testng.annotations.DataProvider
import ru.kkrukhmalev.calculator.base.BaseTokenVisitorTest
import ru.kkrukhmalev.calculator.tokens.Token
import ru.kkrukhmalev.calculator.base.TestTokens.`(`
import ru.kkrukhmalev.calculator.base.TestTokens.`)`
import ru.kkrukhmalev.calculator.base.TestTokens.`*`
import ru.kkrukhmalev.calculator.base.TestTokens.`+`
import ru.kkrukhmalev.calculator.base.TestTokens.`-`
import ru.kkrukhmalev.calculator.base.TestTokens.`|`
import ru.kkrukhmalev.calculator.base.TestTokens.toTokens

class ParserVisitorTest : BaseTokenVisitorTest<List<Token>>() {
    override fun createVisitor() = ParserVisitor()

    @DataProvider(name = "positiveData")
    override fun positiveData(): Array<Array<Any>> = arrayOf(
        arrayOf(
            listOf(3, `+`, 2).toTokens(),
            listOf(3, 2, `+`).toTokens()
        ),
        arrayOf(
            listOf(3, `+`, 2, `*`, 32, `-`, 22, `|`, 11).toTokens(),
            listOf(3, 2, 32, `*`, `+`, 22, 11, `|`, `-`).toTokens()
        ),
        arrayOf(
            listOf(`(`, 3, `+`, 2, `)`, `*`, `(`, 33, `-`, 22, `)`, `|`, 11).toTokens(),
            listOf(3, 2, `+`, 33, 22, `-`, `*`, 11, `|`).toTokens()
        ),
    )

    @DataProvider(name = "negativeData")
    override fun negativeData(): Array<Array<Any>> = arrayOf(
        arrayOf(listOf(3, `)`).toTokens(), IllegalStateException())
    )
}