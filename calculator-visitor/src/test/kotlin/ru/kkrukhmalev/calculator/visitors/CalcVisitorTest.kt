package ru.kkrukhmalev.calculator.visitors

import org.testng.annotations.DataProvider
import ru.kkrukhmalev.calculator.base.BaseTokenVisitorTest
import ru.kkrukhmalev.calculator.base.TestTokens.`*`
import ru.kkrukhmalev.calculator.base.TestTokens.`+`
import ru.kkrukhmalev.calculator.base.TestTokens.`-`
import ru.kkrukhmalev.calculator.base.TestTokens.`|`
import ru.kkrukhmalev.calculator.base.TestTokens.toTokens

class CalcVisitorTest : BaseTokenVisitorTest<Int>() {
    override fun createVisitor() = CalcVisitor()

    @DataProvider(name = "positiveData")
    override fun positiveData(): Array<Array<Any>> = arrayOf(
        arrayOf(listOf(3, 2, `+`).toTokens(), 5),
        arrayOf(listOf(3, 2, 32, `*`, `+`, 22, 11, `|`, `-`).toTokens(), 65),
        arrayOf(listOf(3, 2, `+`, 33, 22, `-`, `*`, 11, `|`).toTokens(), 5),
        arrayOf(listOf(10, 9, 8, 7, `-`, `-`, `-`).toTokens(), 2),
        arrayOf(listOf(10, 9, `-`, 8, `-`, 7, `-`).toTokens(), -14),
        arrayOf(listOf(10, 9, `-`, 8, 7, `-`, `-`).toTokens(), 0),
    )

    @DataProvider(name = "negativeData")
    override fun negativeData(): Array<Array<Any>>  = arrayOf(
        arrayOf(listOf(3, `+`).toTokens(), IllegalStateException()),
        arrayOf(listOf(`+`, 3, 2).toTokens(), IllegalStateException()),
        arrayOf(listOf(3, 2).toTokens(), IllegalStateException()),
    )
}