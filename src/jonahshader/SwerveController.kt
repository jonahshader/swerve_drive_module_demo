package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class SwerveController(width: Int, height: Int) {
    private val module = SwerveModule(width / 4.0, height/2.0)
    private val deadband = 0.2

    private var targetRotations = 0.0
    private var flipped = false
    private var mouseAngle = 0.0
    private var inDeadband = false

    // x y, probably from mouse. must be scaled so that min is -1 and max is 1
    fun run(x: Double, y: Double, magnitude: Double) {
        if (magnitude > deadband) {
            mouseAngle = atan2(y, x)
            inDeadband = false
        } else {
            inDeadband = true
        }

        val mouseRotations = mouseAngle / (PI * 2)

        var targetRotationsWrapped = targetRotations

        while (targetRotationsWrapped > 0.5)
            targetRotationsWrapped--
        while (targetRotationsWrapped < -0.5)
            targetRotationsWrapped++

        var smallestDelta = mouseRotations - targetRotationsWrapped
        smallestDelta += if (smallestDelta > 0.5) -1 else if (smallestDelta < -0.5) 1 else 0

        targetRotations += smallestDelta

        while (targetRotations - module.getRealRotations() > 1) {
            targetRotations--
        }
        while (targetRotations - module.getRealRotations() < -1) {
            targetRotations++
        }

        flipped = false
        if (targetRotations - module.getRealRotations() > 0.25) {
            targetRotations -= 0.5
            flipped = true
        } else if (targetRotations - module.getRealRotations() < -0.25) {
            targetRotations += 0.5
            flipped = true
        }

        module.setDrivePower(if (inDeadband) 0.0 else if (flipped) -magnitude else magnitude)
        module.setRobotStrafe(targetRotations)
        module.run()
    }

    fun draw(graphics: PApplet) {
        module.draw(graphics)
        if (inDeadband) {
            graphics.stroke(255f, 255f, 0f, 45f)
            graphics.fill(255f, 255f, 0f, 45f)
        } else {
            graphics.stroke(255f, 255f, 0f, 15f)
            graphics.fill(255f, 255f, 0f, 15f)
        }

        graphics.circle(graphics.width / 2f, graphics.height / 2f, graphics.width * deadband.toFloat())
    }
}