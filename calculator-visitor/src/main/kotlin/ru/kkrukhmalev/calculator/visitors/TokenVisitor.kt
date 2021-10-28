package ru.kkrukhmalev.calculator.visitors

import ru.kkrukhmalev.calculator.tokens.Brace
import ru.kkrukhmalev.calculator.tokens.NumberToken
import ru.kkrukhmalev.calculator.tokens.Operation
import ru.kkrukhmalev.calculator.tokens.Token


interface TokenVisitor<out T> {
    fun visitNumber(number: NumberToken)
    
    fun visitBrace(brace: Brace)
    
    fun visitOperation(operation: Operation)
    
    fun visitAll(tokens: List<Token>): T  
}