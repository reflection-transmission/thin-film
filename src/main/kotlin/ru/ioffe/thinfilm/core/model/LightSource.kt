package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.core.math.WavelengthDomain
import java.io.File

data class LightSource(val name: String, val profile: List<Wavelength>) {

    companion object {
        fun flat(name: String): LightSource =
            LightSource(name, WavelengthDomain.default().range().map { wavelength(it.toDouble() / 1000, 1.0) })

        fun fromDataFile(file: File): LightSource =
            LightSource(file.name, file.useLines { it.toList() }.map { it.split("\t").map(String::toDouble) }
                .map { wavelength(it[0] * 1000, it[1]) })

        private fun wavelength(length: Double, transmitted: Double, polarization: Polarization = Polarization.Normal) =
            Wavelength(length, 0.0, transmitted, 0.0, polarization)

    }

    fun applyDomain(domain: WavelengthDomain): LightSource {
        return LightSource(
            this.name,
            this.profile.filter { it.length >= domain.min.toDouble() / 1000 && it.length <= domain.max.toDouble() / 1000 })
    }

    override fun toString(): String {
        return name
    }
}