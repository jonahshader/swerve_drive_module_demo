package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SwerveModule {
    private val maxSpeed = 0.05

    private var power = 0.0
    private var targetAngle = 0.0
    private var realAngle = 0.0


    fun run() {
        if (targetAngle > realAngle) {
            realAngle += maxSpeed
            if (targetAngle < realAngle)
                realAngle = targetAngle
        } else {
            realAngle -= maxSpeed
            if (targetAngle > realAngle)
                realAngle = targetAngle
        }
    }

    fun draw(graphics: PApplet) {
        graphics.strokeWeight(2f)
        if (power > 0) {
            graphics.stroke(0f, 255f, 0f)
        } else {
            graphics.stroke(255f, 0f, 0f)
        }
        val x: Float = (cos(realAngle) * graphics.width / 2f).toFloat() + graphics.width / 2f
        val y: Float = (sin(realAngle) * graphics.height / 2f).toFloat() + graphics.height / 2f
        graphics.line(graphics.width / 2f, graphics.height / 2f, x, y)
    }

    fun setDrivePower(power: Double) {
        this.power = power
    }

    fun setTargetRotations(targetRotation: Double) {
        targetAngle = targetRotation * PI * 2
    }

    fun getRealRotations() : Double = realAngle / (PI * 2)
}