package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

class Interpolate {

    fun value(xs: DoubleArray, ys: DoubleArray, wavelength: Double): Double {
        return try {
            val closest = Closest(xs).forValue(wavelength)
            val function = SplineInterpolator().interpolate(
                xs.copyOfRange(closest - 1, closest + 1),
                ys.copyOfRange(closest - 1, closest + 1)
            )
            function.value(wavelength)
        } catch (e: Exception) {
            println(e)
            0.0
        }
    }

    private class Closest(private val domain: DoubleArray) {

        fun forValue(value: Double): Int {
            var low = 0
            var high = domain.size - 1
            var mid = 0
            while (low < high) {
                mid = (low + high) / 2
                val midValue = domain[mid]
                if (midValue == value) {
                    return mid
                } else {
                    if (high - low == 1) return if (value > (domain[high] + domain[low]) / 2) high else low
                    if (midValue > value) high = mid else low = mid
                }
            }
            return mid
        }

    }

}

