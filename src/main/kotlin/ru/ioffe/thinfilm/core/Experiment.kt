package ru.ioffe.thinfilm.core

import org.apache.commons.math3.complex.Complex
import org.jetbrains.kotlinx.multik.api.identity
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.jetbrains.kotlinx.multik.ndarray.complex.div
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import ru.ioffe.thinfilm.core.math.TransferMatrix
import ru.ioffe.thinfilm.core.model.*
import ru.ioffe.thinfilm.core.util.ExperimentContext
import kotlin.math.pow

class Experiment(
    private val context: ExperimentContext,
    private val source: LightSource,
    private val layers: MutableList<Layer>
) {

    private val tm = TransferMatrix()

    fun start(name: String) : Spectrum {
        val spectrum = Spectrum(layers[0], source.profile.map(this::film))
        context.spectrums().add(
            ExperimentSeries(
                spectrum,
                name,
                enabled = true,
                imported = false,
                transmission = true,
                reflection = true,
                absorption = false
            )
        )
        context.refresh()
        return spectrum
    }

    private fun film(it: Wavelength): Wavelength {
        val trRf = calculate(layers, it.length)
        return Wavelength(it.length, it.angle, trRf[0], trRf[1])
    }


    private fun calculate(layers: List<Layer>, wavelength: Double): DoubleArray {
        val matrix = m(layers, wavelength)
        return doubleArrayOf(
            (1 / matrix[0, 0]).abs().pow(2) * layers.last().index.n(wavelength) / layers.first().index.n(wavelength),
            (matrix[1, 0] / matrix[0, 0]).abs().pow(2)
        )
    }

    private fun m(layers: List<Layer>, wavelength: Double): D2Array<ComplexDouble> {
        val n = layers.map { it.index.value(wavelength) }
        val incidenceAngle = ComplexDouble(0.0)
        val theta = angles(incidenceAngle, n)
        var m = mk.identity<ComplexDouble>(2)
        for (i in 1 until layers.size - 1) {
            m = m.dot(tm.propagation(n[i], layers[i].depth, wavelength * 1000))
                .dot(tm.refraction(n[1], n[2], theta[i]))
        }
        return tm.refraction(n[0], n[1], theta[0]).dot(m)
    }

    private fun angles(incidence: ComplexDouble, ns: List<ComplexDouble>): Array<ComplexDouble> {
        val thetas = Array(ns.size) { ComplexDouble(0.0) }
        thetas[0] = incidence
        for (i in 1 until thetas.size) {
            thetas[i] = tm.snell(thetas[i - 1], ns[i - 1], ns[i])
        }
        return thetas
    }

    operator fun Complex.times(value: Complex): Complex = this.multiply(value)

    operator fun Complex.plus(value: Complex): Complex = this.add(value)

    operator fun Complex.minus(value: Complex): Complex = this.minus(value)

}


