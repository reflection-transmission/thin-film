package ru.ioffe.thinfilm.import

import ru.ioffe.thinfilm.core.model.ExperimentSeries
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import ru.ioffe.thinfilm.core.util.ExperimentContext
import java.io.File

class Import(private val context: ExperimentContext, private val transmitted: Boolean = false) {

    fun apply(file: File) {
        val lines = file.useLines { it.toList() }
        val data = lines.subList(lines.indexOf("<Data>") + 1, lines.indexOf("<EndData>"))
        val spectrum = Spectrum(Layer(), data.map(this::wavelength))
        context.spectrums().add(ExperimentSeries(spectrum, file.name, enabled = true, imported = true))
        context.refresh()
    }

    private fun wavelength(line: String): Wavelength {
        val data = line.split("\t").map(String::toDouble)
        return if (transmitted) Wavelength(data[0] / 1000, 1.0, data[1], 0.0)
        else Wavelength(data[0] / 1000, 1.0, 0.0, data[1])
    }

}