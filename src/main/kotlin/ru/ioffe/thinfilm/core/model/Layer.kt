package ru.ioffe.thinfilm.core.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.*

class Layer(
    val properties: MaterialProperties = MaterialProperties.Constant(1.0),
    val depth: Double = 1.0,
    val enabled: Boolean = true,
    private val fulfill: Double = 1.0
) {

    private fun d() = depth * 10.0.pow(-9)

    fun m(wavelength: Double): FieldMatrix<Complex> {
        val n = properties.n(wavelength) // complex refractive index
        val k = properties.k(wavelength)
        val lambda = wavelength * 10.0.pow(-6)
        val a = Complex(
            -sin(2 * PI * n * d() / lambda) * sinh(k * d()),
            cos(2 * PI * n * d() / lambda) * cosh(k * d())
        )
        val b = Complex(
            -(k * a(n, lambda, k) - n * b(n, lambda, k)),
            n * a(n, lambda, k) + k * b(n, lambda, k)
        )
        val c = Complex(
            -(k * a(n, lambda, k) - n * b(n, lambda, k)),
            n * a(n, lambda, k) - k * b(n, lambda, k)
        )
        return MatrixUtils.createFieldMatrix(arrayOf(arrayOf(a, b), arrayOf(c, a)))
    }

    private fun b(n: Double, lambda: Double, k: Double) = cos(2 * PI * n * d() / lambda) * sinh(k * d())

    private fun a(n: Double, lambda: Double, k: Double): Double = sin(2 * PI * n * d() / lambda) * cosh(k * d())


    operator fun Complex.times(value: Double): Complex = this.multiply(value)

    operator fun Complex.times(value: Complex): Complex = this.multiply(value)

    operator fun Complex.plus(value: Complex): Complex = this.add(value)

    operator fun Complex.minus(value: Complex): Complex = this.subtract(value)

    operator fun Complex.div(value: Complex): Complex = this.divide(value)

    operator fun Complex.div(value: Double): Complex = this.divide(value)

}