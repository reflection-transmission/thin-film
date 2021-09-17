package ru.ioffe.thinfilm.ui

import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength

class Experiment(val layers: List<Layer>, val wavelengths: WavelengthDomain = WavelengthDomain.default()) {

    fun start(): Result {
        var spectrum = Spectrum(layers[0], wavelengths(1.0), wavelengths(0.0))
        layers.forEach { spectrum = spectrum.apply(it) }
        return Result(spectrum)
    }

    private fun wavelengths(intensity: Double): List<Wavelength> = mutableListOf<Wavelength>().apply {
        for (i in wavelengths.min..wavelengths.max step 1) {
            add(Wavelength(i.toDouble(), 0.0, intensity))
        }
    }

}