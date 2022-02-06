package ru.ioffe.thinfilm.core.util

import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.net.MaterialProperties

class ThinFilmContext {

    private val materials = Registry<Material>(
        default = Material.Defined("Air", MaterialProperties.Constant(1.0)),
        Material.Defined("Air", MaterialProperties.Constant(1.0)),
        Material.Defined("Constant 2.3", MaterialProperties.Constant(2.3)),
        Material.Defined("Constant 1.5", MaterialProperties.Constant(1.5))
    )

    private val spectrums = Registry(default = Spectrum(Layer(), emptyList()))

    fun materials() = materials

    fun spectrums() = spectrums

}