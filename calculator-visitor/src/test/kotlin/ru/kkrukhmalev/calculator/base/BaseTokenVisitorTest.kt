package ru.kkrukhmalev.calculator.base

import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import ru.kkrukhmalev.calculator.tokens.*
import ru.kkrukhmalev.calculator.visitors.TokenVisitor

abstract class BaseTokenVisitorTest<T> {
    private lateinit var tokenVisitor: TokenVisitor<T>

    abstract fun createVisitor(): TokenVisitor<T>

    @BeforeMethod
    fun beforeTest() {
        tokenVisitor = createVisitor()
    }

    @DataProvider(name = "positiveData")
    abstract fun positiveData(): Array<Array<Any>>

    @Test(dataProvider = "positiveData")
    fun positiveTest(tokens: List<Token>, expected: T) {
        val result = tokenVisitor.visitAll(tokens)
        Assert.assertEquals(result, expected)
    }

    @DataProvider(name = "negativeData")
    abstract fun negativeData(): Array<Array<Any>>

    @Test(dataProvider = "negativeData")
    fun negativeTest(tokens: List<Token>, expected: Exception) {
        Assert.assertThrows(expected::class.java) {
            tokenVisitor.visitAll(tokens)
        }
    }
}