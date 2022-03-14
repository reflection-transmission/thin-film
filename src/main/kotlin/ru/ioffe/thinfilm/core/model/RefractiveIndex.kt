package ru.ioffe.thinfilm.core.model

import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import ru.ioffe.thinfilm.core.math.Interpolate

@kotlinx.serialization.Serializable
data class RefractiveIndex(val dispersion: Map<Double, Value>, val constant: Boolean = false) {

    fun n(wavelength: Double): Double {
        return Interpolate().value(
            dispersion.keys.toDoubleArray(),
            dispersion.values.map(Value::n).toDoubleArray(),
            wavelength
        )
    }

    fun k(wavelength: Double): Double {
        return Interpolate().value(
            dispersion.keys.toDoubleArray(),
            dispersion.values.map(Value::k).toDoubleArray(),
            wavelength
        )
    }

    fun value(wavelength: Double) = ComplexDouble(n(wavelength), k(wavelength))

    fun wavelengths(): DoubleArray {
        return dispersion.keys.toDoubleArray()
    }

    @kotlinx.serialization.Serializable
    data class Value(val n: Double, val k: Double)

}
