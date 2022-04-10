package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.core.math.WavelengthDomain
import java.io.File
import kotlin.math.exp
import kotlin.math.pow

data class LightSource(val name: String, val profile: List<Wavelength>) {

    companion object {
        private val h = 6.63 * 10.0.pow(-34)
        private val c = 3 * 10.0.pow(8)
        private val k = 1.38 * 10.0.pow(-23)

        fun flat(name: String): LightSource =
            LightSource(name, WavelengthDomain.default().range().map { wavelength(it.toDouble() / 1000, 1.0) })

        fun blackBody(): LightSource {
            val profile = WavelengthDomain.default().range()
                .map {
                    wavelength(
                        it.toDouble() / 1000, 2 * h * c.pow(2) / it.toDouble().pow(5) *
                                1 / (exp(h * c / it / 10.0.pow(-9) / k / 5777) - 1)
                    )
                }
            val normalized =
                profile.map { it.instance(transmitted = it.transmitted / profile.maxOf { wavelength -> wavelength.transmitted }) }
            return LightSource(
                "Black Body (4000K)",
                normalized
            )
        }

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