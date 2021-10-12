package ru.ioffe.thinfilm.ui

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.pow

class Experiment(
    private val layers: List<Layer>,
    private val wavelengths: WavelengthDomain = WavelengthDomain.default()
) {

    fun start(): Result {
        return Result(
            Spectrum(
                layers.last(),
                wavelengths(1.0).map { Wavelength(it.length, it.angle, intensity(it)[0]) },
                wavelengths(0.0).map { Wavelength(it.length, it.angle, intensity(it)[1]) })
        )
    }

    private fun intensity(it: Wavelength) =
        trRe(it.length, layers.first().properties.complex(it.length), layers.last().properties.complex(it.length))

    private fun trRe(wavelength: Double, ninc: Complex, nout: Complex): DoubleArray {
        val m = m(wavelength)
        val tr = transition(ninc, nout, m)
        val re = reflection(ninc, nout, m)
        println("lambda: $wavelength tr: $tr re: $re")
        return doubleArrayOf(tr, re)
    }

    private fun reflection(
        ninc: Complex,
        nout: Complex,
        m: FieldMatrix<Complex>
    ): Double {
        return ninc.multiply(m.getEntry(0, 0)).subtract(nout.multiply(m.getEntry(1, 1)))
            .add(ninc.multiply(nout).multiply(m.getEntry(0, 1))).subtract(m.getEntry(1, 0)).divide(
                ninc.multiply(m.getEntry(0, 0)).add(nout.multiply(m.getEntry(1, 1)))
                    .add(ninc.multiply(nout).multiply(m.getEntry(0, 1))).add(m.getEntry(1, 0))
            ).abs().pow(1)
    }

    private fun transition(
        ninc: Complex,
        nout: Complex,
        m: FieldMatrix<Complex>
    ): Double {
        return (nout.real / ninc.real) * (ninc.multiply(2).divide(
            ninc.multiply(m.getEntry(0, 0)) //
                .add(nout.multiply(m.getEntry(1, 1))) //
                .add(ninc.multiply(nout).multiply(m.getEntry(0, 1)) //
                    .add(m.getEntry(1, 0)))
        )).abs().pow(2)
    }

    private fun m(wavelength: Double): FieldMatrix<Complex> {
        var m = layers[0].m(wavelength)
        layers.forEach {
            m = m.multiply(it.m(wavelength))
        }
        return m
    }

    private fun wavelengths(intensity: Double): List<Wavelength> = mutableListOf<Wavelength>().apply {
        for (i in wavelengths.min..wavelengths.max step 1) {
            add(Wavelength(i.toDouble() / 1000, 0.0, intensity))
        }
    }

}