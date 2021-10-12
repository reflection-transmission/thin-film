package ru.ioffe.thinfilm.core.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Classes extending this interface should define a way how the optical layer changes the spectrum going through it
 *
 */
abstract class Layer(val properties: MaterialProperties, val depth: Double, val fulfill: Double) {

    abstract fun apply(spectrum: Spectrum): Spectrum

    private fun phi(wavelength: Double): Double = 2 * PI * properties.n(wavelength) * depth / wavelength

    fun m(wavelength: Double): FieldMatrix<Complex> {
        val a = Complex(cos(phi(wavelength)))
        val b = Complex(0.0, sin(phi(wavelength)) / properties.n(wavelength))
        val c = Complex(0.0, sin(phi(wavelength)) * properties.n(wavelength))
        val d = Complex(cos(phi(wavelength)))
        return MatrixUtils.createFieldMatrix(arrayOf(arrayOf(a, b), arrayOf(c, d)))
    }


}