package graph

import drawing.DrawingApi

class EdgeListGraph(private val edges: List<Pair<Int, Int>>, drawingApi: DrawingApi) : Graph(drawingApi) {
    private var _size = edges.flatMap { listOf(it.first, it.second) }.distinct().size

    override fun drawGraph() {
        drawingApi.withApi {
            val circle = toCircle()
            for ((f, t) in edges) {
                drawLine(circle[f], circle[t])
            }
            for (i in circle.indices)
                drawCircle(circle[i], RADIUS, i)
        }
    }

    override val size: Int
        get() = _size
}