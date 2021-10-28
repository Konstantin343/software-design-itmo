import ru.kkrukhmalev.calculator.tokens.StateTokenizer
import ru.kkrukhmalev.calculator.visitors.CalcVisitor
import ru.kkrukhmalev.calculator.visitors.ParserVisitor
import ru.kkrukhmalev.calculator.visitors.PrintVisitor

fun main() {
    val parserVisitor = ParserVisitor()
    val calcVisitor = CalcVisitor()
    val printVisitor = PrintVisitor(System.out)
    
    val input = readLine()!!
    try {
        val tokenizer = StateTokenizer(input.byteInputStream())
        val inputTokens = tokenizer.toList()

        val rpn = parserVisitor.visitAll(inputTokens)
        val result = calcVisitor.visitAll(rpn)
        
        print("Reverse Polish Notation: ")
        printVisitor.visitAll(rpn)
        println()

        print("Calculation result: $result")
    } catch (e: Exception) {
        print(e.message)
    }
}