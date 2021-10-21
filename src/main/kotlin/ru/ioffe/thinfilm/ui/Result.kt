package ru.ioffe.thinfilm.ui

import javafx.beans.property.SimpleStringProperty
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.*

class Result(private val spectrum: Spectrum) {

    fun draw(chart: LineChart<Number, Number>) {
        invalidateChart(chart)
        chart.series("Transmitted") { spectrum.wavelengths.forEach { data(it.length, it.transmitted) } }
        chart.series("Reflected") { spectrum.wavelengths.forEach { data(it.length, it.reflected) } }
        chart.series("Absorbed") { spectrum.wavelengths.forEach { data(it.length, it.absorbed()) } }
    }

    fun out(property: SimpleStringProperty) {
        var result = "Wavelength Transmitted Reflected Absorbed \n"
        spectrum.wavelengths.forEach { result += "${it.length} ${it.transmitted} ${it.reflected} ${it.absorbed()} \n" }
        property.set(result)
    }

    private fun invalidateChart(chart: LineChart<Number, Number>) {
        chart.data.clear()
        val axis = chart.xAxis as NumberAxis
        axis.isAutoRanging = false
        axis.lowerBound = spectrum.wavelengths.first().length - 0.05
        axis.upperBound = spectrum.wavelengths.last().length + 0.05
        axis.tickUnit = 0.01
    }

}