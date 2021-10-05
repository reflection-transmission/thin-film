package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.core.math.SnelliusLaw
import ru.ioffe.thinfilm.net.MaterialProperties

class FilmLayer(properties: MaterialProperties, depth: Double, fulfill: Double) : Layer(properties, depth, fulfill) {

    override fun apply(spectrum: Spectrum): Spectrum =
        Spectrum(this, spectrum.transmitted.map { transmitted(it, spectrum) }, spectrum.reflected.map(this::reflected))

    private fun reflected(it: Wavelength) =
        Wavelength(it.length, -it.angle, reflectedIntensity(it), it.polarization)

    private fun transmitted(it: Wavelength, spectrum: Spectrum) = Wavelength(
        it.length,
        SnelliusLaw().apply(it.angle, spectrum.ambient.properties.n(it.length), properties.n(it.length)),
        transmittedIntensity(it),
        it.polarization
    )

    private fun reflectedIntensity(it: Wavelength): Double {
        return it.intensity + 0.001 * it.length
    }

    private fun transmittedIntensity(it: Wavelength): Double {
        return it.intensity - 0.001 * it.length
    }

}