package ru.kkrukhmalev.calculator.visitors

import ru.kkrukhmalev.calculator.tokens.*
import java.util.*

class CalcVisitor : TokenVisitor<Int> {
    private val stack: Stack<Int> = Stack()

    private val result: Int
        get() = stack.peek()

    override fun visitNumber(number: NumberToken) {
        stack.add(number.value)
    }

    override fun visitBrace(brace: Brace) {
    }

    override fun visitOperation(operation: Operation) {
        if (stack.size < 2)
            throw IllegalStateException("Can't apply operation $operation, there are no arguments")
        val right = stack.pop()
        val left = stack.pop()
        stack.add(
            when (operation) {
                is SumOperation -> left + right
                is SubOperation -> left - right
                is MulOperation -> left * right
                is DivOperation -> left / right
                else -> throw IllegalArgumentException("Unknown operation: ${operation.javaClass.name}")
            }
        )
    }

    override fun visitAll(tokens: List<Token>): Int {
        stack.clear()
        for (token in tokens)
            token.accept(this)
        if (stack.size < 1)
            throw IllegalStateException("Can't calculate result: there are no numbers")
        if (stack.size > 1)
            throw IllegalStateException("Can't calculate result: not all operation ere processed")
        return result
    }
}