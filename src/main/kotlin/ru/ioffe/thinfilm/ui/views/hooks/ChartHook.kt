package ru.ioffe.thinfilm.ui.views.hooks

import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.data
import tornadofx.series
import java.util.function.Consumer

class ChartHook(private val chart: LineChart<Number, Number>) : Consumer<List<Spectrum>> {

    override fun accept(spectrums: List<Spectrum>) {
        invalidateChart()
        spectrums.forEach { spectrum ->
            resizeChart(spectrum)
            chart.series("Transmitted") {
                spectrum.wavelengths.forEach {
                    if (!it.transmitted.isNaN()) data(it.length, it.transmitted)
                }
            }
            chart.series("Reflected") {
                spectrum.wavelengths.forEach {
                    if (!it.reflected.isNaN()) data(it.length, it.reflected)
                }
            }
            chart.series("Absorbed") {
                spectrum.wavelengths.forEach {
                    if (!it.absorbed().isNaN()) data(it.length, it.absorbed())
                }
            }
        }
    }

    private fun resizeChart(spectrum: Spectrum) {
        val axis = chart.xAxis as NumberAxis
        axis.isAutoRanging = false
        axis.lowerBound = spectrum.wavelengths.first().length - 0.05
        axis.upperBound = spectrum.wavelengths.last().length + 0.05
        axis.tickUnit = 0.01
    }

    private fun invalidateChart() {
        chart.data.clear()
    }

}