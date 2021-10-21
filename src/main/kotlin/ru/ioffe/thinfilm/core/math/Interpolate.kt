package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

class Interpolate {

    fun value(x: DoubleArray, y: DoubleArray, wavelength: Double): Double {
        val interpolator = SplineInterpolator()
        return try {
            val closest = closest(wavelength, x.asList())
            val function =
                interpolator.interpolate(
                    x.copyOfRange(closest - 2, closest + 2),
                    y.copyOfRange(closest - 2, closest + 2)
                )
            function.value(wavelength)
        } catch (e: Exception) {
            0.0
        }
    }

    private fun closest(value: Double, wavelengths: List<Double>): Int {
        if (value < wavelengths.first() || value > wavelengths.last()) {
            throw Exception("Out of bounds. (${wavelengths.first()}, ${wavelengths.last()}) does not include $value")
        }
        return search(value, wavelengths)
    }

    private fun search(value: Double, wavelengths: List<Double>): Int {
        var low = 0
        var high = wavelengths.size - 1
        var mid = 0
        while (low < high) {
            mid = (low + high) / 2
            val midValue = wavelengths[mid]
            if (midValue == value) return mid
            else if (midValue > value) low = mid + 1
            else high = mid - 1
        }
        return mid
    }

}

