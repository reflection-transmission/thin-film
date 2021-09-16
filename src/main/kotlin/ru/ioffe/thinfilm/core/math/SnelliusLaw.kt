package ru.ioffe.thinfilm.core.math

import kotlin.math.asin
import kotlin.math.sin

class SnelliusLaw {

    fun apply(theta1: Double, n1: Double, n2: Double): Double = asin(sin(theta1) * n1 / n2)

}