package ru.ioffe.thinfilm.core.math

class Closest(private val domain: DoubleArray) {

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