package ru.ioffe.thinfilm.tests

import ru.ioffe.thinfilm.core.math.Closest
import ru.ioffe.thinfilm.core.math.Interpolate
import kotlin.test.Test
import kotlin.test.assertEquals

class ClosestSearchTest {

    private val wavelengths = DoubleArray(8) { it.toDouble() }

    @Test
    fun integer() {
        assertEquals(wavelengths[2], wavelengths[closest(wavelengths, 2.0)])
    }

    @Test
    fun closeToNext() {
        assertEquals(wavelengths[3], wavelengths[closest(wavelengths, 2.8)])
        assertEquals(wavelengths[4], wavelengths[closest(wavelengths, 3.8)])
    }

    @Test
    fun closeToPrevious() {
        assertEquals(wavelengths[3], wavelengths[closest(wavelengths, 3.4)])
        assertEquals(wavelengths[4], wavelengths[closest(wavelengths, 4.4)])
    }


    private fun closest(wavelengths: DoubleArray, value: Double): Int {
        return Closest(wavelengths).forValue(value)
    }
}