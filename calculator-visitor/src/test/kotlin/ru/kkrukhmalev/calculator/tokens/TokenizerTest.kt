package ru.kkrukhmalev.calculator.tokens

import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import ru.kkrukhmalev.calculator.base.TestTokens.`(`
import ru.kkrukhmalev.calculator.base.TestTokens.`)`
import ru.kkrukhmalev.calculator.base.TestTokens.`*`
import ru.kkrukhmalev.calculator.base.TestTokens.`+`
import ru.kkrukhmalev.calculator.base.TestTokens.`-`
import ru.kkrukhmalev.calculator.base.TestTokens.`|`
import ru.kkrukhmalev.calculator.base.TestTokens.toTokens

class TokenizerTest {
    @DataProvider(name = "positiveData")
    fun positiveData() = arrayOf(
        arrayOf(
            "",
            listOf<Any>()
        ),
        arrayOf(
            "328 + 21 * 4 - 1",
            listOf(328, `+`, 21, `*`, 4, `-`, 1).toTokens()
        ),
        arrayOf(
            "328 + 21 * (4 - 1)",
            listOf(328, `+`, 21, `*`, `(`, 4, `-`, 1, `)`).toTokens()
        ),
        arrayOf(
            "328 + (21     / 4 - 1)  ",
            listOf(328, `+`, `(`, 21, `|`, 4, `-`, 1, `)`).toTokens()
        ),
        arrayOf(
            "(     328 + 21) * (4 - 1)",
            listOf(`(`, 328, `+`, 21, `)`, `*`, `(`, 4, `-`, 1, `)`).toTokens()
        )
    )

    @Test(dataProvider = "positiveData")
    fun positiveTest(text: String, expected: List<Token>) {
        val tokens = StateTokenizer(text.byteInputStream()).toList()
        Assert.assertEquals(tokens, expected)
    }
    
    @DataProvider(name = "negativeData")
    fun negativeData() = arrayOf(
        arrayOf("125 + a", IllegalArgumentException()),
    )
    
    @Test(dataProvider = "negativeData")
    fun negativeTest(text: String, exception: Throwable) {
        Assert.assertThrows(exception::class.java) {
            StateTokenizer(text.byteInputStream()).toList()
        }
    }
}