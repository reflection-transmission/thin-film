package ru.ioffe.thinfilm.core.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.core.math.OdelevskyFormula
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Layer(
    val properties: MaterialProperties,
    private val depth: Double,
    private val fulfill: Double,
    val enabled: Boolean
) {

    private fun phi(wavelength: Double): Double =
        2 * PI * properties.n(wavelength) * depth * 10.0.pow(-9) / (wavelength * 10.0.pow(-6))

    fun m(wavelength: Double): FieldMatrix<Complex> {
        val a = Complex(cos(phi(wavelength)))
        val b = Complex(0.0, sin(phi(wavelength)) / properties.n(wavelength))
        val c = Complex(0.0, sin(phi(wavelength)) * properties.n(wavelength))
        val d = Complex(cos(phi(wavelength)))
        return MatrixUtils.createFieldMatrix(arrayOf(arrayOf(a, b), arrayOf(c, d)))
    }

    fun n(wavelength: Double): Double = OdelevskyFormula().apply(properties.n(wavelength), 1.0, fulfill)

    fun k(wavelength: Double): Double = properties.k(wavelength)

}