package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.complex.Complex
import ru.ioffe.thinfilm.core.model.Layer
import kotlin.math.*

class CharacteristicMatrix(private val layer: Layer) {

    fun calculate(wavelength: Double) {
        val m11 = Complex(
            cos(phi(wavelength)) * cosh(phiPrime(wavelength)),
            sin(phi(wavelength)) * sinh(phiPrime(wavelength))
        )
        val m12 = Complex(
            n(wavelength) * sin(phi(wavelength)) * cosh(phiPrime(wavelength))
        )
        val m22 = Complex(
            cos(phi(wavelength)) * cosh(phiPrime(wavelength)),
            sin(phi(wavelength)) * sinh(phiPrime(wavelength))
        )
    }

    private fun phi(wavelength: Double): Double = 2 * PI * n(wavelength) * depth() / lambda(wavelength)

    private fun phiPrime(wavelength: Double): Double = 2 * PI * k(wavelength) * depth() / lambda(wavelength)

    private fun n(wavelength: Double) = layer.properties.n(wavelength)

    private fun k(wavelength: Double) = layer.properties.k(wavelength)

    private fun lambda(wavelength: Double): Double = wavelength * 10.0.pow(-6)

    private fun depth(): Double = layer.depth * 10.0.pow(-9)

}