package ru.kkrukhmalev.calculator.tokens

import java.io.InputStream

class StateTokenizer(data: InputStream) : Tokenizer {
    companion object {
        private const val END_CHAR = '\uFFFF'
        private val notNumberChars = setOf('(', ')', '+', '-', '*', '/')
    }

    private var state: State = State.Start

    private val dataReader = data.reader()

    private var currentSymbol = nextNotEmptySymbol()

    private var numberAccumulator: Int? = null

    override fun next(): Token? {
        return when (state) {
            State.Start -> {
                return when {
                    currentSymbol == END_CHAR -> {
                        state = State.End
                        return null
                    }
                    currentSymbol in notNumberChars -> {
                        val token = toToken()
                        currentSymbol = nextNotEmptySymbol()
                        return token
                    }
                    currentSymbol.isDigit() -> {
                        state = State.Number
                        numberAccumulator = currentSymbol.digitToInt()
                        currentSymbol = nextNotEmptySymbol()
                        next()
                    }
                    else -> {
                        state = State.Error
                        throw IllegalArgumentException("Illegal input character: $currentSymbol")
                    }
                }
            }
            State.Number -> {
                when {
                    currentSymbol.isDigit() -> {
                        numberAccumulator = numberAccumulator?.times(10)?.plus(currentSymbol.digitToInt())
                        currentSymbol = nextNotEmptySymbol()
                        next()
                    }
                    else -> {
                        state = State.Start
                        val number = numberAccumulator!!
                        numberAccumulator = null
                        return NumberToken(number)
                    }
                }
            }
            State.Error -> throw IllegalArgumentException("Illegal input character: $currentSymbol")
            State.End -> return null
        }
    }

    override fun toList(): List<Token> {
        val result = mutableListOf<Token>()
        var cur = next()
        while (cur != null) {
            result.add(cur)
            cur = next()
        }
        return result
    }

    private fun nextNotEmptySymbol(): Char {
        var currentSymbol = dataReader.read().toChar()
        while (currentSymbol.isWhitespace()) {
            currentSymbol = dataReader.read().toChar()
        }
        return currentSymbol
    }

    private fun toToken() = when (currentSymbol) {
        '(' -> OpenBrace()
        ')' -> CloseBrace()
        '+' -> SumOperation()
        '-' -> SubOperation()
        '*' -> MulOperation()
        '/' -> DivOperation()
        else -> throw IllegalArgumentException("Illegal input character: $currentSymbol")
    }

    private enum class State {
        Start,
        Number,
        Error,
        End
    }
}