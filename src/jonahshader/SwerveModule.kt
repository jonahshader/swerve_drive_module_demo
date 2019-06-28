package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class SwerveModule(private val x: Double, private val y: Double) {
    private val maxSpeed = 0.05

    private var power = 0.0
    private var targetAngle = 0.0
    private var realAngle = 0.0
    private var strafeMagnitude = 0.0
    private var xPivot = 0.0
    private var yPivot = 0.0


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

        var xEnd = (cos(realAngle) * graphics.width / 2f) * abs(power)
        var yEnd = (sin(realAngle) * graphics.width / 2f) * abs(power)
        xEnd += x
        yEnd += y
        graphics.line(x.toFloat(), y.toFloat(), xEnd.toFloat(), yEnd.toFloat())
    }

    fun setDrivePower(power: Double) {
        this.power = power
    }

    fun setRobotStrafe(targetRotation: Double) {
        targetAngle = targetRotation * PI * 2
    }

    fun setRobotRotations(strafeMagnitude: Double) {
        this.strafeMagnitude = strafeMagnitude
    }

    fun setPivotPoint(xPivot: Double, yPivot: Double) {
        this.xPivot = xPivot
        this.yPivot = yPivot
    }

    fun getRealRotations() : Double = realAngle / (PI * 2)
}