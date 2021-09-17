package ru.ioffe.thinfilm.ui

import javafx.scene.chart.LineChart
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.data
import tornadofx.series

class Result(private val spectrum: Spectrum) {

    fun draw(chart: LineChart<Number, Number>) {
        chart.data.clear()
        chart.series("Transmitted") {
            spectrum.transmitted.forEach { data(it.length, it.intensity) }
        }
        chart.series("Reflected") {
            spectrum.reflected.forEach { data(it.length, it.intensity) }
        }
    }

}