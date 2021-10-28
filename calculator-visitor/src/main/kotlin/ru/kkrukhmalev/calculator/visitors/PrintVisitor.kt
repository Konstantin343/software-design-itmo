package ru.kkrukhmalev.calculator.visitors

import ru.kkrukhmalev.calculator.tokens.Brace
import ru.kkrukhmalev.calculator.tokens.NumberToken
import ru.kkrukhmalev.calculator.tokens.Operation
import ru.kkrukhmalev.calculator.tokens.Token
import java.io.OutputStream

class PrintVisitor(outputStream: OutputStream) : TokenVisitor<Unit> {
    private val writer = outputStream.bufferedWriter()
    
    override fun visitNumber(number: NumberToken) {
        writer.write("$number ")
    }

    override fun visitBrace(brace: Brace) {
        writer.write("$brace ")
    }

    override fun visitOperation(operation: Operation) {
        writer.write("$operation ")
    }

    override fun visitAll(tokens: List<Token>) {
        for (token in tokens)
            token.accept(this)
        writer.flush()
    }
}