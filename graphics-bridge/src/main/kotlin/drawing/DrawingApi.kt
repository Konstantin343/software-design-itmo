package drawing

interface DrawingApi {
    fun getDrawingAreaWidth(): Long

    fun getDrawingAreaHeight(): Long

    fun drawCircle(point: Point, radius: Float, index: Int)
    
    fun drawLine(from: Point, to: Point)
    
    fun withApi(action: DrawingApi.() -> Unit)
}