package ru.ioffe.thinfilm.ui

import javafx.scene.chart.LineChart
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.data
import tornadofx.series

class Result(private val spectrum: Spectrum) {

    fun draw(chart: LineChart<Number, Number>) {
        chart.data.clear()
        chart.series("Transmitted") {
            spectrum.wavelengths.forEach { if (!it.transmitted.isNaN()) data(it.length, it.transmitted) }
        }
        chart.series("Reflected") {
            spectrum.wavelengths.forEach { if (!it.reflected.isNaN()) data(it.length, it.reflected) }
        }
        chart.series("Absorbed") {
            spectrum.wavelengths.forEach {
                if (!it.reflected.isNaN() && !it.transmitted.isNaN()) data(
                    it.length,
                    it.absorbed()
                )
            }
        }
    }

}