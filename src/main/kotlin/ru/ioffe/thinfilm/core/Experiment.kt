package ru.ioffe.thinfilm.core

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import org.apache.commons.math3.linear.MatrixUtils
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.*
import ru.ioffe.thinfilm.core.util.ExperimentContext
import kotlin.math.pow

class Experiment(
    private val context: ExperimentContext,
    private val layers: MutableList<Layer>,
    private val ambient: Layer,
    private val substrate: Layer,
    private val wavelengths: WavelengthDomain = WavelengthDomain.default()
) {

    fun start(name: String) {
        layers.removeLast()
        layers.removeFirst()
        context.spectrums().add(
            ExperimentSeries(
                Spectrum(ambient, wavelengths().map(this::film)),
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

    private fun substrate(it: Wavelength) = Substrate(layers.last().properties, substrate, ambient.properties).apply(it)

    private fun film(it: Wavelength): Wavelength {
        val trRf = calculate(layers.filter(Layer::enabled), ambient, substrate, it.length)
        return Wavelength(it.length, it.angle, trRf[0], trRf[1])
    }


    private fun calculate(layers: List<Layer>, inc: Layer, out: Layer, wavelength: Double): DoubleArray {
        val m = m(layers, wavelength)
        val n0 = inc.properties.n(wavelength)
        val nl = out.properties.n(wavelength)
        val kl = out.properties.k(wavelength)
        val m11 = m.getEntry(0, 0)
        val m12 = m.getEntry(0, 1)
        val m21 = m.getEntry(1, 0)
        val m22 = m.getEntry(1, 1)
        val v =
            n0 * m11.real + m21.imaginary - nl * (n0 * m12.imaginary + m22.real) + kl * (n0 * m12.real - m22.imaginary)
        val z =
            n0 * m11.imaginary - m21.real + nl * (n0 * m12.real - m22.imaginary) + kl * (n0 * m12.imaginary + m22.real)
        val x =
            n0 * m11.real - m21.imaginary - nl * (n0 * m12.imaginary - m22.real) + kl * (n0 * m12.real + m22.imaginary)
        val y =
            n0 * m11.imaginary + m21.real + nl * (n0 * m12.real + m22.imaginary) + kl * (n0 * m12.imaginary - m22.real)
        val t = 4 * n0 * nl / (x.pow(2) + y.pow(2))
        val r = (v.pow(2) + z.pow(2)) / (x.pow(2) + y.pow(2))
        return doubleArrayOf(t, r)
    }

    private fun m(layers: List<Layer>, wavelength: Double): FieldMatrix<Complex> {
        var m = layers[0].m(wavelength)
        layers.drop(1).forEach {
            m = m.multiply(it.m(wavelength))
        }
        return m
    }

    private fun wavelengths(): List<Wavelength> = mutableListOf<Wavelength>().apply {
        for (i in wavelengths.min..wavelengths.max step 1) {
            add(Wavelength(i.toDouble() / 1000, 0.0, 1.0, 0.0))
        }
    }

}


