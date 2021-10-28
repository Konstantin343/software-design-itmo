package ru.kkrukhmalev.calculator.tokens

import ru.kkrukhmalev.calculator.visitors.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor<*>)
}

abstract class BaseToken(private val string: String) : Token {
    override fun toString() = string

    override fun equals(other: Any?) = other is Token && toString() == other.toString()
    
    override fun hashCode() = string.hashCode()
}

class NumberToken(val value: Int) : BaseToken(value.toString()) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visitNumber(this)
}

abstract class Brace(string: String) : BaseToken(string) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visitBrace(this)
}

class OpenBrace : Brace("(")
class CloseBrace : Brace(")")

abstract class Operation(string: String, val priority: Int) : BaseToken(string) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visitOperation(this)
}

class SumOperation : Operation("+", 0)
class SubOperation : Operation("-", 0)
class MulOperation : Operation("*", 1)
class DivOperation : Operation("/", 1)