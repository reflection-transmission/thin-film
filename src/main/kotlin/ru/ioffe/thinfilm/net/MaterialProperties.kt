package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.ioffe.thinfilm.core.math.Complex
import ru.ioffe.thinfilm.core.math.RefractiveIndex

@Serializable
sealed class MaterialProperties {

    @kotlinx.serialization.Transient
    protected val coefficients = mutableMapOf<Double, RefractiveIndex>()

    fun n(wavelength: Double): Double {
        return coefficients.get(wavelength)?.n ?: 0.0
    }

    fun k(wavelength: Double): Double {
        return coefficients.get(wavelength)?.k ?: 0.0
    }

    fun complex(wavelength: Double) : Complex {
        return Complex(n(wavelength), k(wavelength))
    }

    fun wavelengths(): List<Double> {
        return coefficients.keys.toList()
    }

    @Serializable
    @SerialName("tabulated n")
    data class TabulatedN(val data: String) : MaterialProperties() {

        init {
            val split = data.split(" ")
            for (i in split.indices step 2) {
                coefficients.put(split[i].toDouble(), RefractiveIndex(split[i + 1].trim().toDouble(), 0.0))
            }
        }

    }

    @Serializable
    @SerialName("tabulated nk")
    data class TabulatedNK(val data: String) : MaterialProperties() {

        init {
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 3) {
                val wavelength = split[i].toDouble()
                val n = split[i + 1].trim().toDouble()
                val k = split[i + 2].trim().toDouble()
                coefficients.put(wavelength, RefractiveIndex(n, k))
            }
        }

    }

    @Serializable
    @SerialName("tabulated k")
    data class TabulatedK(val data: String) : MaterialProperties() {

        init {
            val split = data.split(" ")
            for (i in split.indices step 2) {
                coefficients.put(split[i].toDouble(), RefractiveIndex(0.0, split[i + 1].trim().toDouble()))
            }
        }

    }

}
