package ru.ioffe.thinfilm.ui

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.databinding.FilmLayerModel
import tornadofx.*

class Workbench(private val registry: MaterialRegistry) : View() {

    private val layers = mutableListOf<FilmLayerModel>().asObservable()
    private val from = SimpleIntegerProperty(400)
    private val to = SimpleIntegerProperty(1600)

    override val root = gridpane {
        row {
            vbox {
                hbox {
                    button("Append Layer").action { layers.add(FilmLayerModel(1.0, 1.0, 0)) }
                    button("Tail Layer").action {
                        if (layers.size > 1) layers.remove(
                            layers.size - 2,
                            layers.size - 1
                        )
                    }
                    textfield(from)
                    textfield(to)
                }
                tableview(layers) {
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    useMaxWidth = true
                    isEditable = true
                    column("depth (nm)", FilmLayerModel::depthProperty).makeEditable()
                    column("fulfill", FilmLayerModel::fulfillProperty).makeEditable()
                    column("material", FilmLayerModel::materialProperty).makeEditable()
                        .useComboBox(registry.indexes().asObservable()).cellFormat {
                            text = registry.get(it!! as Int).name
                            isFillWidth = true
                        }
                }
            }
            vbox {
                val chart = linechart("Material n and k", NumberAxis(), NumberAxis()) {
                    series("n") {
                        val material = load()
                        material.wavelengths().forEach { data(it, material.n(it)) }
                    }
                    series("k") {
                        val material = load()
                        material.wavelengths().forEach { data(it, material.k(it)) }
                    }
                    setCreateSymbols(false)
                }
                button("Run").action {
                    Experiment(
                        layers.map { it.layer(registry) },
                        WavelengthDomain(from.get(), to.get())
                    ).start().draw(chart)
                }
            }
        }
    }

    private fun load(): MaterialProperties {
        val library = Library()
        val shelves = library.fetch().shelves
        return library.entry(shelves.get(0).content.get(0).content.get(0)).data[0]
    }
}