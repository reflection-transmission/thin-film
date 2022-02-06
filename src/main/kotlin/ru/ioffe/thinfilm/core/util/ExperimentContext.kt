package ru.ioffe.thinfilm.core.util

import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.model.ExperimentSeries
import ru.ioffe.thinfilm.core.model.Spectrum
import ru.ioffe.thinfilm.net.MaterialProperties
import java.util.function.Consumer

class ExperimentContext {

    private val materials = Registry<Material>(
        default = Material.Defined("Air", MaterialProperties.Constant(1.0)),
        Material.Defined("Air", MaterialProperties.Constant(1.0)),
        Material.Defined("Constant 2.3", MaterialProperties.Constant(2.3)),
        Material.Defined("Constant 1.5", MaterialProperties.Constant(1.5))
    )

    private val spectrums = Registry(
        default = ExperimentSeries(
            Spectrum(Layer(), emptyList()), "default",
            enabled = false,
            imported = false
        )
    )

    private val hooks = mutableListOf<Consumer<List<Spectrum>>>()

    fun materials() = materials

    fun spectrums() = spectrums

    fun hooks() = hooks

    fun refresh() {
        hooks.forEach { action ->
            action.accept(spectrums.values().filter(ExperimentSeries::enabled).map(ExperimentSeries::spectrum))
        }
    }

}