package jonahshader

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2 {
    private var x = 0.0
    private var y = 0.0

    private var direction = 0.0
    private var magnitude = 0.0

    fun getX() : Double = x
    fun getY() : Double = y
    fun getDirection() : Double = direction
    fun getMagnitude() : Double = magnitude

    fun setComponents(x: Double, y: Double) {
        this.x = x
        this.y = y
        computeVector()
    }

    fun setDirectionMagnitude(direction: Double, magnitude: Double) {
        this.direction = direction
        this.magnitude = magnitude
    }

    // calculate vector using the components
    private fun computeVector() {
        direction = atan2(y, x)
        magnitude = sqrt(x * x + y * y)
    }

    // calculate components using the vector
    private fun computeComponents() {
        x = cos(direction) * magnitude
        y = sin(direction) * magnitude
    }

    fun multiplyMagnitude(magnitude: Double) {
        this.magnitude *= magnitude
        computeComponents()
    }

    // adds another vector to this vector
    fun add(otherVector: Vector2) {
        this.x += otherVector.x
        this.y += otherVector.y
        computeVector()
    }

    // rotates this vector by radians
    fun rotate(radians: Double) {
        direction += radians
        computeComponents()
    }
}