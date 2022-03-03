package ru.ioffe.thinfilm.core.math

import com.github.ajalt.colormath.model.SRGB
import com.github.ajalt.colormath.model.XYZ
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import kotlin.math.*

class Color(private val spectrum: Spectrum) {

    fun toRGB(): String {
        val xyz = spectrum.wavelengths.filter { it.length > .38 && it.length < .78 }.toXYZ()
        val color = XYZ(xyz[0], xyz[1], xyz[2])
        return color.toRGB(SRGB).toHex()
    }

    private fun List<Wavelength>.toXYZ(): D1Array<Double> {
        val x = this.sumOf { it.reflected * x((it.length * 1000).roundToInt()) }
        val y = this.sumOf { it.reflected * y((it.length * 1000).roundToInt()) }
        val z = this.sumOf { it.reflected * z((it.length * 1000).roundToInt()) }
        val sum = x + y + z
        return mk.ndarray(mk[x / sum, y / sum, z / sum])
    }

    private fun x(lambda: Int) = 0.398 * exp(-1250 * (ln((lambda + 570.1) / 1014).pow(2))) + 1.132 * exp(
        -234 * (ln((1138 - lambda) / 743.5).pow(2))
    )

    private fun y(lambda: Int) = 1.011 * exp(-0.5 * (((lambda - 556.1) / 46.14).pow(2)))

    private fun z(lambda: Int) = 2.060 * exp(-32 * (ln((lambda - 265.8) / 180.4).pow(2)))

}