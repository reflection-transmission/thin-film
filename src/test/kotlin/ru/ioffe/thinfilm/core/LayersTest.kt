package ru.ioffe.thinfilm.core

import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.linspace
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.complex.ComplexDouble
import org.jetbrains.kotlinx.multik.ndarray.complex.div
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.junit.Test
import ru.ioffe.thinfilm.core.math.TransferMatrix
import kotlin.math.pow

class LayersTest {

    @Test
    fun some() {
        val tm = TransferMatrix()
        val n1 = ComplexDouble(1)
        val n2 = ComplexDouble(2.3, 0)
        val n3 = ComplexDouble(1.5)
        val range = mk.linspace<Double>(200, 1600, 141)
        for (i in range) {
            val matrix = tm.refraction(n1, n2, ComplexDouble(0.0)).dot(tm.propagation(n2, 100.0, i))
                .dot(tm.refraction(n2, n3, tm.snell(ComplexDouble(0.0), n1, n2)))
            val tr = (1 / matrix[0, 0]).abs().pow(2) * n3.re / n1.re
            val re = (matrix[1, 0] / matrix[0, 0]).abs().pow(2)
            val ab = 1 - tr - re
            println("$i $tr $re $ab".replace(".", ","))
        }

    }
}