package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.MaterialProperties

class FilmLayer(properties: MaterialProperties, depth: Double, fulfill: Double) : Layer(properties, depth, fulfill) {

    override fun apply(spectrum: Spectrum): Spectrum {
        TODO("Not yet implemented")
    }
}