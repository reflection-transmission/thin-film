package ru.ioffe.thinfilm.ui.views.hooks

import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import ru.ioffe.thinfilm.core.model.Series
import ru.ioffe.thinfilm.ui.ExperimentSeries
import java.util.function.Consumer

class TextHook(private val property: SimpleStringProperty) : Consumer<List<ExperimentSeries>> {

    override fun accept(t: List<ExperimentSeries>) {
        var result = ""
        t.map(ExperimentSeries::series).map(Series::spectrum).forEachIndexed { index, spectrum ->
            result += "Series ${index + 1} \n"
            result += "Wavelength Transmitted Reflected Absorbed \n"
            spectrum.wavelengths.forEach { if (!it.transmitted.isNaN() && !it.reflected.isNaN()) result += "${it.length} ${it.transmitted} ${it.reflected} ${it.absorbed()} \n" }
            result += "========================================= \n"
        }
        property.set(result)
        clipboard(result)
    }

    private fun clipboard(result: String) {
        val clipboardContent = ClipboardContent()
        clipboardContent.putString(result)
        Clipboard.getSystemClipboard().setContent(clipboardContent)
    }
}