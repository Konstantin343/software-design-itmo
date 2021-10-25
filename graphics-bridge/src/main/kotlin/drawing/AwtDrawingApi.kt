package drawing

import java.awt.Color
import java.awt.Frame
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

class AwtDrawingApi : DrawingApi {
    private val frame = Frame()

    private val graphics: Graphics2D
        get() = frame.graphics as Graphics2D

    init {
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })
        frame.setSize(600, 400)
    }

    override fun getDrawingAreaWidth(): Long = frame.width.toLong()

    override fun getDrawingAreaHeight(): Long = frame.height.toLong()

    override fun drawCircle(point: Point, radius: Float, index: Int) {
        val g = graphics
        g.color = Color.BLACK
        g.fill(Ellipse2D.Float(point.x - radius, point.y - radius, 2 * radius, 2 * radius))
        g.color = Color.WHITE
        g.drawString(index.toString(), point.x - radius / 2, point.y + radius / 2)
    }

    override fun drawLine(from: Point, to: Point) {
        val g = graphics
        g.color = Color.BLACK
        g.draw(Line2D.Float(from.x, from.y, to.x, to.y))
    }

    override fun withApi(action: DrawingApi.() -> Unit) {
        frame.isVisible = true
        this.action()
    }
}