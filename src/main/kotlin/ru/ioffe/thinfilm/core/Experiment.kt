package ru.ioffe.thinfilm.core

import org.apache.commons.math3.complex.Complex
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.jetbrains.kotlinx.multik.ndarray.complex.div
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.ExperimentSeries
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import ru.ioffe.thinfilm.core.math.TransferMatrix
import ru.ioffe.thinfilm.core.util.ExperimentContext
import kotlin.math.pow

class Experiment(
    private val context: ExperimentContext,
    private val layers: MutableList<Layer>,
    private val wavelengths: WavelengthDomain = WavelengthDomain.default()
) {

    private val tm = TransferMatrix()

    fun start(name: String) {
        context.spectrums().add(
            ExperimentSeries(
                Spectrum(layers[0], wavelengths().map(this::film)),
                name,
                enabled = true,
                imported = false,
                transmission = true,
                reflection = true,
                absorption = false
            )
        )
        context.refresh()
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
        return tm.refraction(n[0], n[1], incidenceAngle)
            .dot(tm.propagation(n[1], layers[1].depth, wavelength * 1000))
            .dot(tm.refraction(n[1], n[2], tm.snell(incidenceAngle, n[1], n[2])))
            .dot(tm.propagation(n[2], layers[2].depth, wavelength * 1000))
            .dot(tm.refraction(n[2], n[3], tm.snell(tm.snell(incidenceAngle, n[1], n[2]), n[2], n[3])))
    }

    private fun transfer(first: Layer, second: Layer, wavelength: Double): D2Array<ComplexDouble> {
        val n1 = ComplexDouble(first.index.n(wavelength), first.index.k(wavelength))
        val n2 = ComplexDouble(second.index.n(wavelength), second.index.k(wavelength))
        return TransferMatrix().refraction(n1, n2, ComplexDouble(0.0))
    }

    private fun wavelengths(): List<Wavelength> = mutableListOf<Wavelength>().apply {
        for (i in wavelengths.min..wavelengths.max step 1) {
            add(Wavelength(i.toDouble() / 1000, 0.0, 1.0, 0.0))
        }
    }

    operator fun Complex.times(value: Complex): Complex = this.multiply(value)

    operator fun Complex.plus(value: Complex): Complex = this.add(value)

    operator fun Complex.minus(value: Complex): Complex = this.minus(value)

}


