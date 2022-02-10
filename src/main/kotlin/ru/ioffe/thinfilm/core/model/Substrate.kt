package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.core.math.Optics
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.pow

class Substrate(
    private val previous: MaterialProperties,
    private val substrate: Layer,
    private val ambient: MaterialProperties
) {

    private val optics = Optics()

    fun apply(incoming: Wavelength): Wavelength {
        val extinction = optics.buger(
            substrate.depth * 10.0.pow(-9),
            substrate.properties.n(incoming.length),
            substrate.properties.k(incoming.length),
            incoming.length * 10.0.pow(-6)
        )
        val substrateToFilm = optics.fresnelTransmission(
            incoming,
            substrate.properties.n(incoming.length),
            previous.n(incoming.length)
        )
        val substrateToAmbient = optics.fresnelTransmission(
            incoming,
            substrate.properties.n(incoming.length),
            ambient.n(incoming.length)
        )
        val t1 = incoming.transmitted * extinction * substrateToAmbient.transmitted
        val t2 =
            incoming.transmitted * extinction * substrateToAmbient.reflected * extinction * substrateToFilm.reflected * extinction * substrateToAmbient.transmitted
        val t = t1 + t2
        val r =
            incoming.reflected + incoming.transmitted * substrateToAmbient.reflected * extinction.pow(2) * substrateToFilm.transmitted
        return Wavelength(incoming.length, substrateToAmbient.angle, t, r, incoming.polarization)
    }

}