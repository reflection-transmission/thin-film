package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

class Interpolate {

    /**
     * Returns interpolated value y for passed wavelength value using xs and ys points.
     * Uses Apache Commons Maths, for more info check corresponding documentation
     */
    fun value(xs: DoubleArray, ys: DoubleArray, wavelength: Double): Double {
        val closest = Closest(xs).forValue(wavelength)
        return try {
            val function = SplineInterpolator().interpolate(
                xs.copyOfRange(closest - 2, closest + 2),
                ys.copyOfRange(closest - 2, closest + 2)
            )
            function.value(wavelength)
        } catch (e: Exception) {
            ys[closest]
        }
    }

}

