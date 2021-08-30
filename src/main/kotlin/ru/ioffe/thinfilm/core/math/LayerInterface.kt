package ru.ioffe.thinfilm.core.math

import ru.ioffe.thinfilm.core.model.Layer
import kotlin.math.pow

/**
 *
 * Calculates the transition coefficients between two layers using Fresnel formulas
 * assuming all rays are always normal to the film surface.
 *
 */
class LayerInterface(private val first: Layer, private val second: Layer) {

    fun calculate(wavelength: Double): Coefficients {
        return Coefficients(transmittance(wavelength), reflectance(wavelength))
    }

    private fun reflectance(wavelength: Double): Double {
        val n1 = first.material.n(wavelength)
        val n2 = second.material.n(wavelength)
        return ((n2 - n1) / (n2 + n1)).pow(2);
    }

    private fun transmittance(wavelength: Double): Double {
        val n1 = first.material.n(wavelength)
        val n2 = second.material.n(wavelength)
        return 4 * n1 * n2 / ((n2 + n1).pow(2))
    }
}