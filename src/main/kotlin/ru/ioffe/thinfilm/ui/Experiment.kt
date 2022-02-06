package ru.ioffe.thinfilm.ui

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Substrate
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.pow

class Experiment(
    private val layers: MutableList<Layer>,
    private val ambient: Layer,
    private val substrate: Layer,
    private val wavelengths: WavelengthDomain = WavelengthDomain.default()
) {

    fun start(): Drawable {
        layers.removeLast()
        layers.removeFirst()
        return Drawable(Spectrum(ambient, wavelengths().map(this::film).map(this::substrate)))
    }

    private fun substrate(it: Wavelength) = Substrate(substrate.properties, ambient.properties).apply(it)

    private fun film(it: Wavelength) = Wavelength(
        it.length,
        it.angle,
        transition(layers.filter(Layer::enabled), ambient, substrate, it.length),
        reflection(layers.filter(Layer::enabled), ambient, substrate, it.length)
    )

    private fun reflection(layers: List<Layer>, inc: Layer, out: Layer, wavelength: Double): Double {
        val m = m(layers, wavelength)
        val ninc = inc.properties.complex(wavelength)
        val nout = out.properties.complex(wavelength)
        val a = m.getEntry(0, 0)
        val b = m.getEntry(0, 1)
        val c = m.getEntry(1, 0)
        val d = m.getEntry(1, 1)
        return ((ninc * a - nout * d + ninc * nout * b - c) / (ninc * a + nout * d + ninc * nout * b + c)).abs().pow(2)
    }

    private fun transition(layers: List<Layer>, inc: Layer, out: Layer, wavelength: Double): Double {
        val m = m(layers, wavelength)
        val ninc = inc.properties.complex(wavelength)
        val nout = out.properties.complex(wavelength)
        val a = m.getEntry(0, 0)
        val b = m.getEntry(0, 1)
        val c = m.getEntry(1, 0)
        val d = m.getEntry(1, 1)
        return (ninc * 2.0 / (ninc * a + nout * d + ninc * nout * b + c)).abs().pow(2) * (nout.real / ninc.real)
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

    operator fun Complex.times(value: Double): Complex = this.multiply(value)

    operator fun Complex.times(value: Complex): Complex = this.multiply(value)

    operator fun Complex.plus(value: Complex): Complex = this.add(value)

    operator fun Complex.minus(value: Complex): Complex = this.subtract(value)

    operator fun Complex.div(value: Complex): Complex = this.divide(value)

}

