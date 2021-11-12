package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.core.math.Optics
import ru.ioffe.thinfilm.net.MaterialProperties

class Substrate(private val material: MaterialProperties, private val ambient: MaterialProperties) {

    fun apply(wavelength: Wavelength): Wavelength {
        val f = Optics().fresnelTransmission(wavelength, material.n(wavelength.length), ambient.n(wavelength.length))
        val t1 = f.transmitted
        val t2 = wavelength.transmitted
        val r1 = f.reflected
        val r2 = wavelength.reflected
        val r = r1 + t1 * r2 * t1 + t1 * r2 * r1 * r2 * t1
        val t = t1 * (t2 + r2 * r1 * t2 + r2 * r1 * r2 * r1 * t2)
        return Wavelength(wavelength.length, f.angle, t, r, wavelength.polarization)
    }

}