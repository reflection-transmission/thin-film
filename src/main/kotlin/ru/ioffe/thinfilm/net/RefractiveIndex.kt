package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.apache.commons.math3.complex.Complex
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import ru.ioffe.thinfilm.core.math.Interpolate
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Serializable
sealed class RefractiveIndex {

    @kotlinx.serialization.Transient
    protected val dispersion = mutableMapOf<Double, Complex>()

    fun n(wavelength: Double): Double {
        return Interpolate().value(
            dispersion.keys.toDoubleArray(),
            dispersion.values.map(Complex::getReal).toDoubleArray(),
            wavelength
        )
    }

    fun k(wavelength: Double): Double {
        return Interpolate().value(
            dispersion.keys.toDoubleArray(),
            dispersion.values.map(Complex::getImaginary).toDoubleArray(),
            wavelength
        )
    }

    fun value(wavelength: Double): ComplexDouble = ComplexDouble(n(wavelength), k(wavelength))

    fun wavelengths(): List<Double> {
        return dispersion.keys.toList()
    }

    @Serializable
    @SerialName("tabulated n")
    data class TabulatedN(val data: String) : RefractiveIndex() {

        init {
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 2) {
                dispersion[split[i].toDouble()] = Complex(split[i + 1].trim().toDouble(), 0.0)
            }
        }

    }

    @Serializable
    @SerialName("tabulated nk")
    data class TabulatedNK(val data: String) : RefractiveIndex() {

        init {
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 3) {
                val wavelength = split[i].toDouble()
                val n = split[i + 1].trim().toDouble()
                val k = split[i + 2].trim().toDouble()
                dispersion[wavelength] = Complex(n, k)
            }
        }

    }

    @Serializable
    @SerialName("formula 1")
    data class FormulaOne(val wavelength_range: String, val coefficients: String) : RefractiveIndex() {

        init {
            val range = wavelength_range.split(" ").map(String::toDouble).map { (it * 1000).roundToInt() }
            val cs = coefficients.split(" ").map(String::toDouble)
            for (i in range[0]..range[1] step 1) {
                val wavelength = (i * 10.0.pow(-3))
                var value = cs[0] + 1
                for (j in 1 until cs.size step 2) {
                    value += cs[j] * wavelength.pow(2) / (wavelength.pow(2) - cs[j + 1].pow(2))
                }
                dispersion[wavelength] = Complex(sqrt(value), 0.0)
            }
        }
    }

    @Serializable
    @SerialName("formula 2")
    data class FormulaTwo(val wavelength_range: String, val coefficients: String) : RefractiveIndex() {

        init {
            val range = wavelength_range.split(" ").map(String::toDouble).map { (it * 1000).roundToInt() }
            val cs = coefficients.split(" ").map(String::toDouble)
            for (i in range[0]..range[1] step 1) {
                val wavelength = (i * 10.0.pow(-3))
                var value = cs[0] + 1
                for (j in 1 until cs.size step 2) {
                    value += cs[j] * wavelength.pow(2) / (wavelength.pow(2) - cs[j + 1])
                }
                dispersion[wavelength] = Complex(sqrt(value), 0.0)
            }
        }

    }


    @Serializable
    @SerialName("tabulated k")
    data class TabulatedK(val data: String) : RefractiveIndex() {

        init {
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 2) {
                dispersion[split[i].toDouble()] = Complex(0.0, split[i + 1].trim().toDouble())
            }
        }

    }

    data class Constant(val value: Double) : RefractiveIndex() {
        init {
            for (i in 200..2000) {
                dispersion[i.toDouble() / 1000] = Complex(value, 0.0)
            }
        }
    }

}