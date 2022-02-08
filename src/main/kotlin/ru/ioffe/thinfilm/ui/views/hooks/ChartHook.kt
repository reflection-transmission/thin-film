package ru.ioffe.thinfilm.ui.views.hooks

import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.model.ExperimentSeries
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.data
import tornadofx.series
import java.util.function.Consumer

class ChartHook(private val chart: LineChart<Number, Number>) : Consumer<List<ExperimentSeries>> {

    override fun accept(spectrums: List<ExperimentSeries>) {
        invalidateChart()
        spectrums.forEach { series ->
            val spectrum = series.spectrum
            resizeChart(spectrum)
            if (series.transmission) {
                chart.series("Transmitted") {
                    spectrum.wavelengths.forEach {
                        if (!it.transmitted.isNaN()) data(it.length, it.transmitted)
                    }
                }
            }
            if (series.reflection) {
                chart.series("Reflected") {
                    spectrum.wavelengths.forEach {
                        if (!it.reflected.isNaN()) data(it.length, it.reflected)
                    }
                }
            }
            if (series.absorption) {
                chart.series("Absorbed") {
                    spectrum.wavelengths.forEach {
                        if (!it.absorbed().isNaN()) data(it.length, it.absorbed())
                    }
                }
            }
        }
    }

    private fun resizeChart(spectrum: Spectrum) {
        val xAxis = chart.xAxis as NumberAxis
        xAxis.isAutoRanging = false
        xAxis.lowerBound = spectrum.wavelengths.first().length - 0.05
        xAxis.upperBound = spectrum.wavelengths.last().length + 0.05
        xAxis.tickUnit = 0.01
        val yAxis = chart.yAxis as NumberAxis
        yAxis.isAutoRanging = false
        yAxis.lowerBound = 0.0
        yAxis.upperBound = 1.0
        yAxis.tickUnit = 0.01
    }

    private fun invalidateChart() {
        chart.data.clear()
    }

}