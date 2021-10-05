package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import ru.ioffe.thinfilm.net.MaterialProperties

class Interpolate {

    fun value(properties: MaterialProperties, wavelength: Double): Double {
        val interpolator = SplineInterpolator()
        val function = interpolator.interpolate(DoubleArray(0), DoubleArray(0))
        return function.value(wavelength)
    }

    private fun closest(value: Double, count: Int, wavelengths: List<Double>): DoubleArray {
        if (value < wavelengths.first() || value > wavelengths.last()) {
            throw Exception("Out of bounds")
        }
        return DoubleArray(1) { 0.0 }
    }
}

