package ru.ioffe.thinfilm.ui.library

import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import ru.ioffe.thinfilm.net.Shelf

class ChartView(private val library: Library) : LineChart<Number, Number>(NumberAxis(), NumberAxis()) {

    init {
        createSymbols = false

    }

    private fun material(page: Shelf.Book.Page): MaterialProperties {
        return library.entry(page).data[0]
    }

}