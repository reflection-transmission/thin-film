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
            incoming.length
        )
        val filmToSubstrate = optics.fresnelTransmission(
            incoming,
            previous.n(incoming.length),
            substrate.properties.n(incoming.length)
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
        val t1 = filmToSubstrate.transmitted
        val t2 = t1 * extinction * substrateToAmbient.transmitted
        val r1 = filmToSubstrate.reflected
        val r2 = t1 * extinction * substrateToAmbient.reflected * extinction * substrateToFilm.transmitted
        val t = t2 * incoming.transmitted
        val r = incoming.reflected + incoming.transmitted * (r1 + r2)
        return Wavelength(incoming.length, substrateToAmbient.angle, t, r, incoming.polarization)
    }

}