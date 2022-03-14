package ru.ioffe.thinfilm.core.util

import ru.ioffe.thinfilm.core.model.*
import ru.ioffe.thinfilm.net.RefractiveIndexData
import tornadofx.asObservable
import java.util.function.Consumer

/**
 * Context class. Stores current section context.
 */
class Session {

    private val materials = Registry<Material>(
        default = Material("Air", RefractiveIndexData.Constant(1.0).index()),
        Material("Air", RefractiveIndexData.Constant(1.0).index()),
        Material("Constant 2.3", RefractiveIndexData.Constant(2.3).index()),
        Material("Constant 1.5", RefractiveIndexData.Constant(1.5).index())
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

    private val layers = mutableListOf(
        LayerModel(LayerModel.Ambient, 1.0, materials.get(materials.values()[0])),
        LayerModel(LayerModel.Film, 200.0, materials.get(materials.values()[1])),
        LayerModel(LayerModel.Substrate, 1.0, materials.get(materials.values()[2]))
    ).asObservable()

    private val sources = Registry(default = LightSource.flat("default"), LightSource.flat("Flat Spectrum"))

    private val hooks = mutableListOf<Consumer<List<ExperimentSeries>>>()

    fun materials() = materials

    fun spectrums() = spectrums

    fun sources() = sources

    fun hooks() = hooks

    fun layers() = layers

    fun refresh() {
        hooks.forEach { action ->
            action.accept(spectrums.values().filter(ExperimentSeries::enabled))
        }
    }

}