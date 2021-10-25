package graph

import drawing.DrawingApi

class MatrixGraph(private val matrix: Array<Array<Int>>, drawingApi: DrawingApi) : Graph(drawingApi) {
    override fun drawGraph() {
        drawingApi.withApi {
            val circle = toCircle()
            for (i in matrix.indices) {
                for (j in matrix.indices) {
                    if (matrix[i][j] == 1) {
                        drawLine(circle[i], circle[j])
                    }
                }
            }
            for (i in circle.indices)
                drawCircle(circle[i], RADIUS, i)
        }
    }

    override val size: Int
        get() = matrix.size
}