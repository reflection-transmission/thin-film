package ru.ioffe.thinfilm.core.model

import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import ru.ioffe.thinfilm.core.math.TransferMatrix
import ru.ioffe.thinfilm.net.RefractiveIndex

class Layer(
    val depth: Double,
    val index: RefractiveIndex = RefractiveIndex.Constant(1.0)
) {

    fun matrix(wavelength: Double): D2Array<ComplexDouble> {
        return TransferMatrix().propagation(ComplexDouble(index.n(wavelength), index.k(wavelength)), depth, wavelength.times(1000))
    }

}