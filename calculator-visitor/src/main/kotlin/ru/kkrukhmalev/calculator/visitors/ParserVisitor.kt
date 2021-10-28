package ru.kkrukhmalev.calculator.visitors

import ru.kkrukhmalev.calculator.tokens.*
import java.util.*

class ParserVisitor : TokenVisitor<List<Token>> {
    private val result: MutableList<Token> = mutableListOf()

    private val stack: Stack<Token> = Stack()

    private val rpn: List<Token>
        get() = result.toList()

    override fun visitNumber(number: NumberToken) {
        result.add(number)
    }

    override fun visitBrace(brace: Brace) {
        when (brace) {
            is OpenBrace -> stack.add(brace)
            is CloseBrace -> {
                if (stack.isEmpty())
                    throw IllegalStateException("Missing open brace")
                var cur = stack.pop()
                while (cur !is OpenBrace) {
                    result.add(cur)
                    if (stack.isEmpty())
                        throw IllegalStateException("Missing open brace")
                    cur = stack.pop()
                }
            }
        }
    }

    override fun visitOperation(operation: Operation) {
        if (stack.isNotEmpty()) {
            var cur = stack.peek()
            while (cur is Operation && cur.priority >= operation.priority ) {
                result.add(stack.pop())
                if (stack.isEmpty()) break
                cur = stack.peek()
            }
        }
        stack.add(operation)
    }

    override fun visitAll(tokens: List<Token>): List<Token> {
        result.clear()
        for (token in tokens)
            token.accept(this)
        while (stack.isNotEmpty())
            result.add(stack.pop())
        return rpn
    }
}