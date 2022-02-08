package ru.ioffe.thinfilm.core.math

import ru.ioffe.thinfilm.core.model.Polarization
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.*

class Optics {

    private fun snelliusAngle(theta1: Double, n1: Double, n2: Double) = asin(sin(theta1) * n1 / n2)

    fun effectiveIndex(e1: Double, e2: Double, volume: Double): Double {
        val a = (volume - 0.5) * (e1 * e1 - e2 * e2)
        return sqrt(a + sqrt(a.pow(2) + e1 * e1 * e2 * e2))
    }

    fun buger(depth: Double, n: Double, k: Double, wavelength: Double): Double =
        Math.E.pow(-1 * depth * (4 * Math.PI * n * k / wavelength))

    fun fresnelTransmission(wavelength: Wavelength, n1: Double, n2: Double): Wavelength {
        val theta1 = wavelength.angle
        val theta2 = snelliusAngle(theta1, n1, n2)
        val tr = when (wavelength.polarization) {
            is Polarization.Normal -> transition(n2, n1, theta1, theta2)
            is Polarization.Parallel -> transition(n1, n2, theta1, theta2)
            else -> transition(n2, n1, theta1, theta2) / 2 + transition(n1, n2, theta1, theta2) / 2
        }
        val re = when (wavelength.polarization) {
            is Polarization.Normal -> reflection(n1, n2, theta1, theta2)
            is Polarization.Parallel -> reflection(n2, n1, theta1, theta2)
            else -> reflection(n2, n1, theta1, theta2) / 2 + reflection(n1, n2, theta1, theta2) / 2
        }
        return Wavelength(wavelength.length, theta2, tr, re, wavelength.polarization)
    }

    private fun transition(n1: Double, n2: Double, theta1: Double, theta2: Double) =
        n1 * cos(theta2) / n2 / cos(theta1) * (2 * n2 * cos(theta1) / (n1 * cos(theta1) + n2 * cos(theta2))).pow(2)

    private fun reflection(n1: Double, n2: Double, theta1: Double, theta2: Double) =
        ((n1 * cos(theta1) - n2 * cos(theta2)) / (n1 * cos(theta1) + n2 * cos(theta2))).pow(2)


}