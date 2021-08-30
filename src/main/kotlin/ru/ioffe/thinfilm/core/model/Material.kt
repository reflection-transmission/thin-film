package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.MaterialProperties

class Material(val name: String, val properties : MaterialProperties = MaterialProperties.TabulatedN("1 1")) {

    fun n(wavelength: Double): Double {
        return properties.n(wavelength)
    }
}
