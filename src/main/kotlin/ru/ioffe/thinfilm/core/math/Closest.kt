package ru.ioffe.thinfilm.core.math

class Closest(private val domain: DoubleArray) {

    /**
     * Finds index of the closest item to input value in sorted DoubleArray using binary search
     *
     * @param value input value
     */
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
                if (high - low == 1) return closest(value, high, low)
                if (midValue > value) high = mid else low = mid
            }
        }
        return mid
    }

    private fun closest(value: Double, high: Int, low: Int) =
        if (value > (domain[high] + domain[low]) / 2) high else low

}