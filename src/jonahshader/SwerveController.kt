package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class SwerveController {
    private val module = SwerveModule()

    private var targetRotations = 0.0
    private var flipped = false

    // x y, probably from mouse. must be scaled so that min is -1 and max is 1
    fun run(x: Double, y: Double) {
        val mouseDistance = sqrt(x * x + y * y)
        val mouseAngle = atan2(y, x)

        val mouseRotations = mouseAngle / (PI * 2)

//        var deltaRotations = mouseRotations - targetRotations
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



        module.setDrivePower(if (flipped) -mouseDistance else mouseDistance)
        module.setTargetRotations(targetRotations)
        module.run()
    }

    fun draw(graphics: PApplet) {
        module.draw(graphics)
    }
}