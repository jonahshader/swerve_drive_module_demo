package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.atan2


class SwerveController(width: Int, height: Int) {
    companion object {
        const val deadband = 0.2
    }

    private var modules = arrayOf<SwerveModule>()
    private val pivotPoint = PivotPoint(0.0, 0.0)

    init {
        modules += SwerveModule(-0.25, 0.0, pivotPoint)
        modules += SwerveModule(0.0, 0.25, pivotPoint)
        modules += SwerveModule(0.25, 0.0, pivotPoint)
        modules += SwerveModule(0.0, -0.25, pivotPoint)
    }

    private var mouseAngle = 0.0
    private var inDeadband = false

    // x y, probably from mouse. must be scaled so that min is -1 and max is 1
    fun run(x: Double, y: Double, strafeMagnitude: Double, rotationVelocity: Double, pivotX: Double, pivotY: Double) {
        inDeadband = strafeMagnitude < 0.2
        mouseAngle = atan2(y, x)
        var largestMagnitude = 1.0
        for (i in modules.indices) {
            modules[i].setDriveParams(mouseAngle / (PI * 2), strafeMagnitude, rotationVelocity)
            modules[i].run()
            if (modules[i].getMagnitude() > largestMagnitude)
                largestMagnitude = modules[i].getMagnitude()
        }

        if (largestMagnitude > 1.0) {
            val invLargestMagnitude = 1.0 / largestMagnitude
            for (i in modules.indices) {
                modules[i].multiplyMagnitude(invLargestMagnitude)
            }
        }

        pivotPoint.x = pivotX
        pivotPoint.y = pivotY
    }

    fun draw(graphics: PApplet) {
        for (i in modules.indices) {
            modules[i].draw(graphics)
        }

        if (inDeadband) {
            graphics.stroke(255f, 255f, 0f, 45f)
            graphics.fill(255f, 255f, 0f, 45f)
        } else {
            graphics.stroke(255f, 255f, 0f, 15f)
            graphics.fill(255f, 255f, 0f, 15f)
        }

        graphics.circle(graphics.width / 2f, graphics.height / 2f, graphics.width * deadband.toFloat())

        graphics.stroke(0f, 0f, 0f)
        graphics.fill(0f, 0f, 0f)
        graphics.strokeWeight(3f)

        val pivotPointX = ((pivotPoint.x.toFloat() * .5f) + .5f) * graphics.width
        val pivotPointY = ((pivotPoint.y.toFloat() * .5f) + .5f) * graphics.height
        graphics.point(pivotPointX, pivotPointY)
    }
}