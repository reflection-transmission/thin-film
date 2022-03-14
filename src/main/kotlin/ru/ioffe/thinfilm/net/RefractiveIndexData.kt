package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.ioffe.thinfilm.core.model.RefractiveIndex

@Serializable
sealed class RefractiveIndexData {

    abstract fun index(): RefractiveIndex

    @Serializable
    @SerialName("tabulated n")
    data class TabulatedN(val data: String) : RefractiveIndexData() {

        override fun index(): RefractiveIndex {
            val dispersion = mutableMapOf<Double, RefractiveIndex.Value>()
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 2) {
                dispersion[split[i].toDouble()] = RefractiveIndex.Value(split[i + 1].trim().toDouble(), 0.0)
            }
            return RefractiveIndex(dispersion)
        }

    }

    @Serializable
    @SerialName("tabulated nk")
    data class TabulatedNK(val data: String) : RefractiveIndexData() {

        override fun index(): RefractiveIndex {
            val dispersion = mutableMapOf<Double, RefractiveIndex.Value>()
            val split = data.replace("\n", " ").split(" ").filterNot { it.isEmpty() }
            for (i in split.indices step 3) {
                val wavelength = split[i].toDouble()
                val n = split[i + 1].trim().toDouble()
                val k = split[i + 2].trim().toDouble()
                dispersion[wavelength] = RefractiveIndex.Value(n, k)
            }
            return RefractiveIndex(dispersion)
        }

    }

    @Serializable
    @SerialName("formula 1")
    data class FormulaOne(val wavelength_range: String, val coefficients: String) : RefractiveIndexData() {

        override fun index(): RefractiveIndex {
//            val range = wavelength_range.split(" ").map(String::toDouble).map { (it * 1000).roundToInt() }
//            val cs = coefficients.split(" ").map(String::toDouble)
//            for (i in range[0]..range[1] step 1) {
//                val wavelength = (i * 10.0.pow(-3))
//                var value = cs[0] + 1
//                for (j in 1 until cs.size step 2) {
//                    value += cs[j] * wavelength.pow(2) / (wavelength.pow(2) - cs[j + 1].pow(2))
//                }
//                dispersion[wavelength] = ComplexDouble(sqrt(value), 0.0)
//            }
            TODO("Not yet implemented")
        }
    }

    @Serializable
    @SerialName("formula 2")
    data class FormulaTwo(val wavelength_range: String, val coefficients: String) : RefractiveIndexData() {

        override fun index(): RefractiveIndex {
//            val range = wavelength_range.split(" ").map(String::toDouble).map { (it * 1000).roundToInt() }
//            val cs = coefficients.split(" ").map(String::toDouble)
//            for (i in range[0]..range[1] step 1) {
//                val wavelength = (i * 10.0.pow(-3))
//                var value = cs[0] + 1
//                for (j in 1 until cs.size step 2) {
//                    value += cs[j] * wavelength.pow(2) / (wavelength.pow(2) - cs[j + 1])
//                }
//                dispersion[wavelength] = ComplexDouble(sqrt(value), 0.0)
//            }
            TODO("Not yet implemented")
        }

    }

    data class Constant(val value: Double) : RefractiveIndexData() {

        override fun index(): RefractiveIndex {
            val dispersion = mutableMapOf<Double, RefractiveIndex.Value>()
            for (i in 200..2000) {
                dispersion[i.toDouble() / 1000] = RefractiveIndex.Value(value, 0.0)
            }
            return RefractiveIndex(dispersion, true)
        }
    }

}
