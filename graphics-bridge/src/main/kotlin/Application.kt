import drawing.AwtDrawingApi
import drawing.JavaFxDrawingApi
import graph.EdgeListGraph
import graph.Graph
import graph.MatrixGraph
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) error()

    val drawingApi = when (args[1]) {
        "javafx" -> JavaFxDrawingApi()
        "awt" -> AwtDrawingApi()
        else -> error()
    }

    val graph = when (args[0]) {
        "matrix" -> MatrixGraph(readMatrix("matrix.txt"), drawingApi)
        "edges" -> EdgeListGraph(readEdges("edges.txt"), drawingApi)
        else -> error()
    }

    graph.drawGraph()
}

fun error(): Nothing {
    println("Usage: <app-path> <graph> <api>\n" +
            " graph - matrix/edges\n" +
            " api   - javafx/awt")
    exitProcess(1)
}

fun getResource(path: String) =
    Graph::class.java.classLoader.getResource(path)!!.toString().removePrefix("file:")

fun readEdges(path: String) =
    File(getResource(path)).readLines().map {
        val ft = it.split("\\s+".toRegex())
        ft[0].toInt() to ft[1].toInt()
    }

fun readMatrix(path: String) =
    File(getResource(path)).readLines().map {
        it.split("\\s+".toRegex()).map { a -> a.toInt() }.toTypedArray()
    }.toTypedArray()

