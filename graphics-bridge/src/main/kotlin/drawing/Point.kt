package drawing

data class Point(val x: Float, val y: Float) {
    operator fun plus(another: Point): Point {
        return Point(x + another.x, y + another.y)
    }

    operator fun minus(another: Point): Point {
        return Point(x - another.x, y - another.y)
    }
}