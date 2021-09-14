package ru.ioffe.thinfilm.core.math

import ru.ioffe.thinfilm.core.model.Transmission
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class FresnelFormula {

    fun nonPolar(n1: Double, n2: Double, theta1: Double): Transmission {
        val theta2 = snellius(theta1, n1, n2)
        return Transmission(
            transition(n2, n1, theta1, theta2) / 2 + transition(n1, n2, theta1, theta2) / 2,
            reflection(n2, n1, theta1, theta2) / 2 + reflection(n1, n2, theta1, theta2) / 2,
            theta2
        )
    }

    fun normal(n1: Double, n2: Double, theta1: Double): Transmission {
        val theta2 = snellius(theta1, n1, n2)
        return Transmission(transition(n1, n2, theta1, theta2), reflection(n1, n2, theta1, theta2), theta2)
    }

    fun parallel(n1: Double, n2: Double, theta1: Double): Transmission {
        val theta2 = snellius(theta1, n1, n2)
        return Transmission(transition(n2, n1, theta1, theta2), reflection(n2, n1, theta1, theta2), theta2)
    }

    private fun transition(n1: Double, n2: Double, theta1: Double, theta2: Double) =
        n1 * cos(theta2) / n2 / cos(theta1) * (2 * n2 * cos(theta1) / (n1 * cos(theta1) + n2 * cos(theta2))).pow(2)

    private fun reflection(n1: Double, n2: Double, theta1: Double, theta2: Double) =
        ((n1 * cos(theta1) - n2 * cos(theta2)) / (n1 * cos(theta1) + n2 * cos(theta2))).pow(2)

    private fun snellius(theta1: Double, n1: Double, n2: Double) = asin(sin(theta1) * n1 / n2)


}
