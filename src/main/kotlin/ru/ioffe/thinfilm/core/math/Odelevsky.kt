package ru.ioffe.thinfilm.core.math

import kotlin.math.pow
import kotlin.math.sqrt

class Odelevsky {

    fun apply(e1: Double, e2: Double, volume: Double): Double {
        val a = (volume - 0.5) * (e1 - e2)
        return a + sqrt(a.pow(2) + e1 * e2)
    }

}