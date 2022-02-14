package ru.ioffe.thinfilm.core.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldMatrix
import ru.ioffe.thinfilm.core.math.CharacteristicMatrix
import ru.ioffe.thinfilm.net.MaterialProperties
import kotlin.math.pow

class Layer(
    val properties: MaterialProperties = MaterialProperties.Constant(1.0),
    val depth: Double = 1.0,
    val enabled: Boolean = true,
    private val fulfill: Double = 1.0
) {

    fun m(wavelength: Double): FieldMatrix<Complex> = CharacteristicMatrix(this).calculate(wavelength)

}