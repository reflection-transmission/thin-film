package ru.ioffe.thinfilm.core

import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.junit.Test
import ru.ioffe.thinfilm.core.math.TransferMatrix

class DummyTest {

    @Test
    fun positive() {
        val tm = TransferMatrix()
        println(tm.refraction(ComplexDouble(1.5), ComplexDouble(2.5), ComplexDouble(0.0)))
        println(tm.refraction(ComplexDouble(2.5), ComplexDouble(3.5, 3.5), ComplexDouble(0.0)))
    }
}