package jonahshader

import processing.core.PApplet
/*
some ideas to implement:

-scale module magnitudes down if they are not near their target angle
val scaleDown = 0.25 - maxDelta
maybe square it??

this will reduce the error from driving while the module is not at the target yet. should make the driving feel more predictable, and would be crutial in autonomous.

 */

fun main() {
    PApplet.main(App::class.java)
}

class App : PApplet() {

    private val screenWidth = 640
    private val screenHeight = 640

    private val swerveController = SwerveController(screenWidth, screenHeight)

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
        val magnitude = kotlin.math.sqrt(x * x + y * y)
        swerveController.run(x, y, magnitude)
        swerveController.draw(this)

        strokeWeight(1f)
        stroke(127f, 127f, 127f)
        line(width / 2f, height / 2f, mouseX.toFloat(), height - mouseY.toFloat())
    }

}