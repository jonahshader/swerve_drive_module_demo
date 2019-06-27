package jonahshader

import processing.core.PApplet

fun main() {
    PApplet.main(App::class.java)
}

class App : PApplet() {

    private val swerveController = SwerveController()

    override fun settings() {
        size(640, 640)
    }

    override fun setup() {

    }

    override fun draw() {
        background(255)
        scale(1f, -1f)
        translate(0f, -height.toFloat())
        val x = ((mouseX.toDouble() / width) - 0.5) * 2.0
        val y = -((mouseY.toDouble() / height) - 0.5) * 2.0
        swerveController.run(x, y)
        swerveController.draw(this)
    }

}