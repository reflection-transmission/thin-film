package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.core.model.Layer
import kotlin.math.*

class CharacteristicMatrix(private val layer: Layer) {

    fun calculate(wavelength: Double): Array<Complex> {
        val m11 = Complex(
            cos(phi(wavelength)) * cosh(phiPrime(wavelength)),
            sin(phi(wavelength)) * sinh(phiPrime(wavelength))
        )
        val m12 = Complex(
                (n(wavelength) * paramA(wavelength) + k(wavelength) * paramB(wavelength)) / paramC(wavelength),
                (k(wavelength) * paramA(wavelength) - n(wavelength) * paramB(wavelength)) / paramC(wavelength)
            )

        val m21 = Complex(
                n(wavelength) * paramA(wavelength) - k(wavelength) * paramB(wavelength),
                -k(wavelength) * paramA(wavelength) - n(wavelength) * paramB(wavelength)
            )

        val m22 = Complex(
            cos(phi(wavelength)) * cosh(phiPrime(wavelength)),
            sin(phi(wavelength)) * sinh(phiPrime(wavelength))
        )
        return arrayOf(m11, m12, m21, m22)
    }

    private fun phi(wavelength: Double): Double = 2 * PI * n(wavelength) * depth() / lambda(wavelength)

    private fun phiPrime(wavelength: Double): Double = 2 * PI * k(wavelength) * depth() / lambda(wavelength)

    private fun n(wavelength: Double) = layer.properties.n(wavelength)

    private fun k(wavelength: Double) = layer.properties.k(wavelength)

    private fun lambda(wavelength: Double): Double = wavelength * 10.0.pow(-6)

    private fun depth(): Double = layer.depth * 10.0.pow(-9)

    private fun paramA(wavelength: Double): Double = sin(phi(wavelength)) * cosh(phiPrime(wavelength))

    private fun paramB(wavelength: Double): Double = cos(phi(wavelength)) * sinh(phiPrime(wavelength))

    private fun paramC(wavelength: Double) = n(wavelength).pow(2) + k(wavelength).pow(2)
}