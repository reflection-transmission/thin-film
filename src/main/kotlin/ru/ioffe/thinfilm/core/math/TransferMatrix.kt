package ru.ioffe.thinfilm.core.math

import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.complex.*
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import ru.ioffe.thinfilm.core.model.Polarization
import kotlin.math.*

class TransferMatrix {

    private val i = ComplexDouble(0, 1)

    fun propagation(n: ComplexDouble, d: Double, wavelength: Double): D2Array<ComplexDouble> {
        val delta = d * 2 * PI * n / wavelength
        return mk.ndarray(
            mk[
                    mk[exp(-i * delta), ComplexDouble.zero],
                    mk[ComplexDouble.zero, exp(i * delta)]]
        )
    }

    fun refraction(n1: ComplexDouble, n2: ComplexDouble, angle: ComplexDouble): D2Array<ComplexDouble> {
        val tr = transmission(angle, n1, n2, Polarization.Normal)
        val re = reflection(angle, n1, n2, Polarization.Normal)
        return 1 / tr * mk.ndarray(
            mk[
                    mk[ComplexDouble.one, re],
                    mk[re, ComplexDouble.one]]
        )
    }

    // Optics

    fun snell(theta1: ComplexDouble, n1: ComplexDouble, n2: ComplexDouble) = asin(sin(theta1) * n1 / n2)

    private fun transmission(
        theta1: ComplexDouble,
        n1: ComplexDouble,
        n2: ComplexDouble,
        pol: Polarization
    ): ComplexDouble {
        val theta2 = snell(theta1, n1, n2)
        return when (pol) {
            is Polarization.Normal -> 2 * n1 * cos(theta1) / (n1 * cos(theta1) + n2 * cos(theta2))
            is Polarization.Parallel -> 2 * n1 * cos(theta1) / (n2 * cos(theta1) + n1 * cos(theta2))
            else -> throw NotImplementedError()
        }
    }

    private fun reflection(
        theta1: ComplexDouble,
        n1: ComplexDouble,
        n2: ComplexDouble,
        pol: Polarization
    ): ComplexDouble {
        val theta2 = snell(theta1, n1, n2)
        return when (pol) {
            is Polarization.Normal -> (n1 * cos(theta1) - n2 * cos(theta2)) / (n1 * cos(theta1) + n2 * cos(theta2))
            is Polarization.Parallel -> (n2 * cos(theta1) - n1 * cos(theta2)) / (n2 * cos(theta1) + n1 * cos(theta2))
            else -> throw NotImplementedError()
        }
    }

    // Maths

    private fun exp(value: ComplexDouble) = ComplexDouble(cos(value.im), sin(value.im)) * exp(value.re)

    private fun ln(value: ComplexDouble): ComplexDouble = ln(value.re) + i * value.angle()

    // Trigonometry

    private fun asin(value: ComplexDouble): ComplexDouble =
        if (value.im == 0.0)
            ComplexDouble(asin(value.re))
        else
            1 / i * ln(i * value + sqrt((1 - value * value).abs()) * exp(i / 2 * (1 - value * value).angle()))

    private fun cos(value: ComplexDouble): ComplexDouble =
        if (value.im == 0.0)
            ComplexDouble(cos(value.re))
        else
            (exp(-i * value) + exp(i * value)) / 2.0

    private fun sin(value: ComplexDouble): ComplexDouble =
        if (value.im == 0.0)
            ComplexDouble(sin(value.re))
        else
            i * (exp(-i * value) - exp(i * value)) / 2.0

}