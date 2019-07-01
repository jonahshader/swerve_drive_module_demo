package jonahshader

import jonahshader.SwerveController.Companion.deadband
import processing.core.PApplet
import kotlin.math.*

class SwerveModule(private val x: Double, private val y: Double, private val pivotPoint: PivotPoint) {
    private val lineLengthScale = 0.5
    private val maxSpeed = 0.05 * PI / 3.0             // max radians this module can rotate per frame

    private var magnitude = 0.0             // speed and polarity of module
    private var strafeMagnitude = 0.0       // just the magnitude of the strafing
    private var targetStrafeDirection = 0.0       // target direction in range -1 to 1. this is the end goal direction this module should be driving
    private var targetAngle = 0.0           // this is the actual direction the module should be facing
    private var realAngle = 0.0             // current raw angle the module is at at any given time
    private var robotRotationVelocity = 0.0    // from -1 to 1, target robot rotation speed. (how fast this module will rotate around its pivot point)
    private var targetRotations = 0.0
    private var flipped = false

    fun run() {
        runAngleCalculation()
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

    private fun runAngleCalculation() {
        // calculate components
        val targetStrafeDirectionXComp = cos(targetStrafeDirection) * strafeMagnitude
        val targetStrafeDirectionYComp = sin(targetStrafeDirection) * strafeMagnitude

        // calculate target robot rotation direction in rads (which is 90 degrees offset from the angle between the
        // module and the pivot point
        val targetRotationDirection = atan2(y - pivotPoint.y, x - pivotPoint.x) - (PI * .5)
        // calculate components
        val targetRotationDirectionXComp = cos(targetRotationDirection) * robotRotationVelocity
        val targetRotationDirectionYComp = sin(targetRotationDirection) * robotRotationVelocity

        val compositeX = targetStrafeDirectionXComp + targetRotationDirectionXComp
        val compositeY = targetStrafeDirectionYComp + targetRotationDirectionYComp
        val compositeTargetDirectionRotations = atan2(compositeY, compositeX) / (PI * 2)
        val compositeMagnitude = sqrt(compositeX * compositeX + compositeY * compositeY)
        magnitude = compositeMagnitude


        var targetRotationsWrapped = targetRotations

        while (targetRotationsWrapped > 0.5)
            targetRotationsWrapped--
        while (targetRotationsWrapped < -0.5)
            targetRotationsWrapped++

        var smallestDelta = compositeTargetDirectionRotations - targetRotationsWrapped
        smallestDelta += if (smallestDelta > 0.5) -1 else if (smallestDelta < -0.5) 1 else 0

        targetRotations += smallestDelta

        while (targetRotations - getRealRotations() > 1) {
            targetRotations--
        }
        while (targetRotations - getRealRotations() < -1) {
            targetRotations++
        }

        flipped = false
        if (targetRotations - getRealRotations() > 0.25) {
            targetRotations -= 0.5
            flipped = true
        } else if (targetRotations - getRealRotations() < -0.25) {
            targetRotations += 0.5
            flipped = true
        }

        targetAngle = targetRotations * PI * 2

    }

    fun draw(graphics: PApplet) {
        graphics.strokeWeight(2f)
        if (flipped) {
            graphics.stroke(255f, 0f, 0f)
        } else {
            graphics.stroke(0f, 255f, 0f)
        }

        var xEnd = (cos(realAngle) * graphics.width / 2f) * abs(magnitude) * lineLengthScale
        var yEnd = (sin(realAngle) * graphics.width / 2f) * abs(magnitude) * lineLengthScale
        xEnd += (x + 0.5) * graphics.width
        yEnd += (y + 0.5) * graphics.height
        graphics.line(((x + 0.5) * graphics.width).toFloat(), ((y + 0.5) * graphics.height).toFloat(), xEnd.toFloat(), yEnd.toFloat())
    }

    // magnitude must be within the range [0, 1]
    // robotRotationVelocity must be within the range [0, 1]
    // robotStrafeRotations can be any number
    fun setDriveParams(robotStrafeRotations: Double, strafeMagnitude: Double, robotRotationVelocity: Double) {
        // update robotStrafeRotations and magnitude only if the magnitude is above the deadband threshold
        if (strafeMagnitude > deadband) {
            this.strafeMagnitude = strafeMagnitude
            targetStrafeDirection = robotStrafeRotations * PI * 2
        } else {
            // if magnitude is within the deadband, set it to 0
            this.strafeMagnitude = 0.0
        }
        this.robotRotationVelocity = if (abs(robotRotationVelocity) > deadband) robotRotationVelocity else 0.0
    }

    // this is used when SwerveController finds the largest magnitude to be over 1.0.
    // SwerveController then multiplies by the inverse to scale down all module magnitudes
    fun multiplyMagnitude(otherMagnitude: Double) {
        magnitude *= otherMagnitude
    }

    private fun getRealRotations() : Double = realAngle / (PI * 2)
    fun getMagnitude(): Double = magnitude
}