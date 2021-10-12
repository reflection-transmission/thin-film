package ru.ioffe.thinfilm.core.math

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import kotlin.math.pow
import kotlin.math.roundToInt

class Interpolate {

    fun value(x: DoubleArray, y: DoubleArray, wavelength: Double, count: Int = 5): Double {
        val interpolator = SplineInterpolator()
        try {
            val closest = closest(wavelength, count, x.asList())
            val function =
                interpolator.interpolate(x.copyOfRange(closest[0], closest[4]), y.copyOfRange(closest[0], closest[4]))
            return function.value(wavelength)
        } catch (e:Exception) {
            return 0.0
        }
    }

    private fun closest(value: Double, count: Int, wavelengths: List<Double>): IntArray {
        if (value < wavelengths.first() || value > wavelengths.last()) {
            throw Exception("Out of bounds. (${wavelengths.first()}, ${wavelengths.last()}) does not include $value")
        }
        val result = mutableListOf<Int>()
        val found = search(value, wavelengths)
        for (i in 0 until count) {
            val alternation = (-1).toDouble().pow(i.toDouble()).roundToInt()
            val index = found + i * alternation
            if (index >= 0 && index < wavelengths.size) result.add(index)
        }
        return result.toIntArray()
    }

    private fun search(value: Double, wavelengths: List<Double>): Int {
        var low = 0
        var high = wavelengths.size - 1
        var mid = 0
        while (low <= high) {
            mid = (low + high) / 2
            val midValue = wavelengths[mid]
            if (midValue == value) return mid
            else if (midValue > value) low = mid + 1
            else high = mid - 1
        }
        return mid
    }

}

