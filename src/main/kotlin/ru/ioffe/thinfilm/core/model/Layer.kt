package ru.ioffe.thinfilm.core.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.core.math.Optics
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Layer(
    val properties: MaterialProperties = MaterialProperties.Constant(1.0),
    private val depth: Double = 1.0,
    val enabled: Boolean = true,
    private val fulfill: Double = 1.0,
    private val profile: Map<Double, Double> = mapOf(Pair(1.0, 1.0))
) {

    private fun phi(wavelength: Double, depth: Double): Double =
        2 * PI * properties.n(wavelength) * depth * 10.0.pow(-9) / (wavelength * 10.0.pow(-6))

    fun m(wavelength: Double): FieldMatrix<Complex> {
        var m = identityMatrix()
        var previous = 0.0
        for (point in profile.keys.sorted()) {
            val part = depth * point - previous
            previous = part
            val phi = phi(wavelength, part)
            val n = n(wavelength, profile[point]!!)
            val a = Complex(cos(phi))
            val b = Complex(0.0, sin(phi) / n)
            val c = Complex(0.0, sin(phi) * n)
            val d = Complex(cos(phi))
            m = m.multiply(MatrixUtils.createFieldMatrix(arrayOf(arrayOf(a, b), arrayOf(c, d))))
        }
        return m
    }

    private fun identityMatrix() =
        MatrixUtils.createFieldMatrix(arrayOf(arrayOf(Complex(1.0), Complex(0.0)), arrayOf(Complex(0.0), Complex(1.0))))

    private fun n(wavelength: Double, fulfill: Double) = Optics().effectiveIndex(properties.n(wavelength), 1.0, fulfill)

}