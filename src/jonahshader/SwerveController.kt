package jonahshader

import processing.core.PApplet
import kotlin.math.PI
import kotlin.math.atan2


class SwerveController(width: Int, height: Int) {
    companion object {
        const val deadband = 0.2
    }
    private var modules = arrayOf<SwerveModule>()
//    private val module = SwerveModule(-0.25, 0.0)

    init {
        modules += SwerveModule(-0.25, 0.0)
        modules += SwerveModule(0.0, 0.25)
        modules += SwerveModule(0.25, 0.0)
        modules += SwerveModule(0.0, -0.25)
    }

    private var mouseAngle = 0.0
    private var inDeadband = false

    // x y, probably from mouse. must be scaled so that min is -1 and max is 1
    fun run(x: Double, y: Double, magnitude: Double) {
        inDeadband = magnitude < 0.2
        mouseAngle = atan2(y, x)
        for (i in modules.indices) {
            modules[i].setDriveParams(mouseAngle / (PI * 2), magnitude, 0.0)
            modules[i].run()
        }
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
    }
}