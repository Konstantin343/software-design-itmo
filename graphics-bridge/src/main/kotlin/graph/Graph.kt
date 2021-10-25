package graph

import drawing.DrawingApi
import drawing.Point
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

abstract class Graph(
    /**
     * Bridge to drawing API
     */
    protected val drawingApi: DrawingApi,
) {
    abstract fun drawGraph()

    abstract val size: Int
    
    companion object {
        const val GRAPH_RADIUS_DELTA = 35
        const val RADIUS = 10f
    }

    fun toCircle(): List<Point> {
        val width = drawingApi.getDrawingAreaWidth().toFloat()
        val height = drawingApi.getDrawingAreaHeight().toFloat()
        val graphRadius = min(width, height) / 2.0 - GRAPH_RADIUS_DELTA
        val middle = Point(width / 2, height / 2)
        val angle = Math.PI * 2 / this.size
        return (0 until this.size).map {
            middle + Point(
                -graphRadius.toFloat() * sin(it * angle).toFloat(),
                graphRadius.toFloat() * cos(it * angle).toFloat()
            )
        }
    }
}