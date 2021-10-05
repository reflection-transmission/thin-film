package ru.ioffe.thinfilm.core.model

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import ru.ioffe.thinfilm.core.math.Complex
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Classes extending this interface should define a way how the optical layer changes the spectrum going through it
 *
 */
abstract class Layer(val properties: MaterialProperties, val depth: Double, val fulfill: Double) {

    abstract fun apply(spectrum: Spectrum) : Spectrum

    private fun phi(wavelength: Double): Double = 2 * PI * properties.n(wavelength) * depth / wavelength

    fun m(wavelength: Double): D2Array<Complex> {
        val a = Complex(cos(phi(wavelength)))
        val b = Complex(0.0, sin(phi(wavelength)) / properties.n(wavelength))
        val c = Complex(0.0, sin(phi(wavelength)) * properties.n(wavelength))
        val d = Complex(cos(phi(wavelength)))
        return mk.ndarray(mk[mk[a, b], mk[c, d]])
    }



}