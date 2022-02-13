package ru.ioffe.thinfilm.core

import ru.ioffe.thinfilm.core.math.Closest
import kotlin.test.Test
import kotlin.test.assertEquals

class ClosestTest {

    @Test
    fun positive() {
        val domain = DoubleArray(5, Int::toDouble)
        assertEquals(1.0, domain[Closest(domain).forValue(1.1)])
        assertEquals(2.0, domain[Closest(domain).forValue(1.9)])
        assertEquals(3.0, domain[Closest(domain).forValue(3.0)])
    }
}