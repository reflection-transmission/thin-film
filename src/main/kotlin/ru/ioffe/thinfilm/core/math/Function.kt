package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator

class Function(val data: Map<Double, Double>) {

    fun get(argument: Double): Double {
        return SplineInterpolator().interpolate(data.keys.toDoubleArray(), data.values.toDoubleArray()).value(argument)
    }

}