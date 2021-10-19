package ru.ioffe.thinfilm.core.math

import ru.ioffe.thinfilm.core.model.Polarization
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.cos
import kotlin.math.pow

class FresnelFormula {

    fun apply(wavelength: Wavelength, n1: Double, n2: Double): Wavelength {
        val theta1 = wavelength.angle
        val theta2 = SnelliusLaw().apply(theta1, n1, n2)
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
