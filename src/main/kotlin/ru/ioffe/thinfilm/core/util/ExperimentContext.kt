package ru.ioffe.thinfilm.core.util

import ru.ioffe.thinfilm.core.model.*
import ru.ioffe.thinfilm.net.RefractiveIndex
import java.util.function.Consumer

/**
 * Context class. Stores current section context.
 */
class ExperimentContext {

    private val materials = Registry<Material>(
        default = Material.Defined("Air", RefractiveIndex.Constant(1.0)),
        Material.Defined("Air", RefractiveIndex.Constant(1.0)),
        Material.Defined("Constant 2.3", RefractiveIndex.Constant(2.3)),
        Material.Defined("Constant 1.5", RefractiveIndex.Constant(1.5))
    )

    private val spectrums = Registry(
        default = ExperimentSeries(
            Spectrum(Layer(depth = 100.0), emptyList()), "default",
            enabled = false,
            transmission = true,
            reflection = true,
            absorption = true
        )
    )

    private val sources = Registry(default = LightSource.flat("default"), LightSource.flat("Flat Spectrum"))

    private val hooks = mutableListOf<Consumer<List<ExperimentSeries>>>()

    fun materials() = materials

    fun spectrums() = spectrums

    fun sources() = sources

    fun hooks() = hooks

    fun refresh() {
        hooks.forEach { action ->
            action.accept(spectrums.values().filter(ExperimentSeries::enabled))
        }
    }

}