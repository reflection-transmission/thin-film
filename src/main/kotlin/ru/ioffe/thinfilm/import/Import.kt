package ru.ioffe.thinfilm.import

import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.core.model.Wavelength
import java.io.File

class Import(private val transmitted: Boolean = false) {

    fun apply(file: File): Spectrum {
        val lines = file.useLines { it.toList() }
        val data = lines.subList(lines.indexOf("<Data>") + 1, lines.indexOf("<EndData>"))
        return Spectrum(Layer(), data.map(this::wavelength))
    }

    private fun wavelength(line: String): Wavelength {
        val data = line.split("\t").map(String::toDouble)
        return if (transmitted) Wavelength(data[0] / 1000, 1.0, data[1], 0.0)
        else Wavelength(data[0] / 1000, 1.0, 0.0, data[1])
    }

}