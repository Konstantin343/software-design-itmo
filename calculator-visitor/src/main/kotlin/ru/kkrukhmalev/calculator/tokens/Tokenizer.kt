package ru.kkrukhmalev.calculator.tokens

interface Tokenizer {
    fun next(): Token?
    
    fun toList(): List<Token>
}