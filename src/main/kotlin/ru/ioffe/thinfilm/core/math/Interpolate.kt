package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

class Interpolate {

    fun value(xs: DoubleArray, ys: DoubleArray, wavelength: Double): Double {
        return try {
            val closest = Closest(xs).forValue(wavelength)
            val function = SplineInterpolator().interpolate(
                xs.copyOfRange(closest - 2, closest + 2),
                ys.copyOfRange(closest - 2, closest + 2)
            )
            function.value(wavelength)
        } catch (e: Exception) {
            println(e)
            0.0
        }
    }

}

