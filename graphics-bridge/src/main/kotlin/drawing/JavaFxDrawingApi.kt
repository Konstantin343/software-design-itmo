package drawing

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage

class JavaFxDrawingApi : DrawingApi {
    class App : Application() {
        companion object {
            val canvas = Canvas(600.0, 400.0)
        }

        override fun start(primaryStage: Stage) {
            val root = Group()
            root.children.add(canvas)
            primaryStage.scene = Scene(root)
            primaryStage.show()
        }
    }

    private val graphics
        get() = App.canvas.graphicsContext2D

    override fun getDrawingAreaWidth() = App.canvas.width.toLong()

    override fun getDrawingAreaHeight() = App.canvas.height.toLong()

    override fun drawCircle(point: Point, radius: Float, index: Int) {
        graphics.fill = Color.BLACK
        graphics.fillOval(
            point.x - radius.toDouble(), point.y - radius.toDouble(),
            2 * radius.toDouble(), 2 * radius.toDouble()
        )
        graphics.fill = Color.WHITE
        graphics.fillText(index.toString(), point.x.toDouble() - radius / 2, point.y.toDouble() + radius / 2)
    }

    override fun drawLine(from: Point, to: Point) {
        graphics.fill = Color.BLACK
        graphics.strokeLine(from.x.toDouble(), from.y.toDouble(), to.x.toDouble(), to.y.toDouble())
    }

    override fun withApi(action: DrawingApi.() -> Unit) {
        this.action()
        Application.launch(App::class.java)
    }
}