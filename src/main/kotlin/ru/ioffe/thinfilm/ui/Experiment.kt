package ru.ioffe.thinfilm.ui

import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import ru.ioffe.thinfilm.core.math.Complex
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.pow

class Experiment(private val layers: List<Layer>, private val wavelengths: WavelengthDomain = WavelengthDomain.default()) {

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
        val tr =
            (nout.re / ninc.re) * (2 * ninc / (ninc * m[1, 1] + nout * m[2, 2] + ninc * nout * m[1, 2] + m[2, 1])).absolute()
                .pow(2)
        val re =
            ((ninc * m[1, 1] - nout * m[2, 2] + ninc * nout * m[1, 2] - m[2, 1]) / (ninc * m[1, 1] + nout * m[2, 2] + ninc * nout * m[1, 2] + m[2, 1])).absolute()
                .pow(2)
        return doubleArrayOf(tr, re)
    }

    private fun m(wavelength: Double): D2Array<Complex> {
        var m = layers[0].m(wavelength)
        layers.forEach {
            m *= it.m(wavelength)
        }
        return m
    }

    private fun wavelengths(intensity: Double): List<Wavelength> = mutableListOf<Wavelength>().apply {
        for (i in wavelengths.min..wavelengths.max step 1) {
            add(Wavelength(i.toDouble(), 0.0, intensity))
        }
    }

}