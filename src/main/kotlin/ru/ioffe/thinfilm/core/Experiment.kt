package ru.ioffe.thinfilm.core

import org.apache.commons.math3.complex.Complex
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
                Spectrum(ambient, wavelengths().map(this::film).map(this::substrate)),
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
        val m = matrixM(layers, wavelength)
        val n0 = inc.properties.n(wavelength)
        val nl = out.properties.n(wavelength)
        val kl = out.properties.k(wavelength)
        val m11 = m[0]
        val m12 = m[1].divide(Complex.I)
        val m21 = m[2].divide(Complex.I)
        val m22 = m[3]
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

    private fun m(layers: List<Layer>, wavelength: Double): Array<Complex> {
        val ms = mutableListOf<Array<Complex>>()
        ms.add(layers[0].m(wavelength))
        layers.drop(1).forEach {
            val m = ms.last()
            val mi = it.m(wavelength)
            val m11 =
                Complex(
                    m[0].real * mi[0].real - m[0].imaginary * mi[0].imaginary - m[1].real * mi[2].real + m[1].imaginary * mi[2].imaginary,
                    m[0].real * mi[0].imaginary + m[0].imaginary * mi[0].real - m[1].real * mi[2].imaginary - m[1].imaginary * mi[2].real
                )
            val m12 =
                Complex(
                    m[0].real * mi[1].real - m[0].imaginary * mi[1].imaginary + m[1].real * mi[3].real - m[1].imaginary * mi[3].imaginary,
                    m[0].real * mi[1].imaginary + m[0].imaginary * mi[1].real + m[1].real * mi[3].imaginary + m[1].imaginary * mi[3].real
                )
            val m21 =
                Complex(
                    m[3].real * mi[2].real - m[3].imaginary * mi[2].imaginary + m[2].real * mi[0].real - m[2].imaginary * mi[0].imaginary,
                    m[3].real * mi[2].imaginary + m[3].imaginary * mi[2].real + m[2].real * mi[0].imaginary + m[2].imaginary * mi[0].real
                )
            val m22 =
                Complex(
                    m[3].real * mi[3].real - m[3].imaginary * mi[3].imaginary - m[2].real * mi[1].real + m[2].imaginary * mi[1].imaginary,
                    m[3].real * mi[3].imaginary + m[3].imaginary * mi[3].real - m[2].real * mi[1].imaginary - m[2].imaginary * mi[1].real
                )
            ms.add(arrayOf(m11, m12, m21, m22))
        }
        return ms.last()
    }

    fun matrixM(layers: List<Layer>, wavelength: Double): Array<Complex> {
        var m = layers.get(0).m(wavelength)
        m[1] = Complex.I * m[1]
        m[2] = Complex.I * m[2]
        layers.drop(1).forEach {
            val mi = it.m(wavelength)
            mi[1] = Complex.I * mi[1]
            mi[2] = Complex.I * mi[2]
            m = arrayOf(
                (m[0] * mi[0]) + (m[1] * mi[2]),
                (m[0] * mi[1]) + (m[1] * mi[3]),
                (m[2] * mi[0]) + (m[3] * mi[2]),
                (m[2] * mi[1]) + (m[3] * mi[3])
            ) // Just a multiplication
        }
        return m
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


