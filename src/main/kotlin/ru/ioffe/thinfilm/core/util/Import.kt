package ru.ioffe.thinfilm.core.util

import ru.ioffe.thinfilm.core.model.*
import ru.ioffe.thinfilm.ui.ExperimentSeries
import java.io.File

class Import(private val context: Session, private val transmitted: Boolean = false) {

    fun apply(file: File) {
        val lines = file.useLines { it.toList() }
        val data = lines.subList(lines.indexOf("<Data>") + 1, lines.indexOf("<EndData>"))
        val spectrum = Spectrum(Layer(depth = 100.0, Material.air()), data.map(this::wavelength))
        context.spectrums().add(
            ExperimentSeries(
                Series(spectrum, file.name),
                enabled = true,
                transmission = transmitted,
                reflection = !transmitted,
                absorption = false
            )
        )
        context.refresh()
    }

    private fun wavelength(line: String): Wavelength {
        val data = line.split("\t").map(String::toDouble)
        return if (transmitted) Wavelength(data[0] / 1000, 1.0, data[1], 0.0)
        else Wavelength(data[0] / 1000, 1.0, 0.0, data[1])
    }

}