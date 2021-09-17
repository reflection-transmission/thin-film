package ru.ioffe.thinfilm.ui

import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.ui.databinding.FilmLayerModel
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import tornadofx.*

class Workbench() : View() {

    private val layers = listOf(
        FilmLayerModel(1, 1.0, 1.0, Material("ITO250"), true),
        FilmLayerModel(2, 1.0, 1.0, Material("ITO250"), true),
        FilmLayerModel(3, 1.0, 1.0, Material("ITO250"), true),
        FilmLayerModel(4, 1.0, 1.0, Material("ITO250"), true),
        FilmLayerModel(5, 1.0, 1.0, Material("ITO250"), true)
    ).asObservable()

    private val materials = listOf(
        Material("ITO250"),
        Material("Glass"),
        Material("Al2O3"),
        Material("Blood")
    ).asObservable()

    override val root = gridpane {
        row {
            tableview(layers) {
                gridpaneColumnConstraints {
                    percentWidth = 50.0
                }
                useMaxWidth = true
                isEditable = true
                readonlyColumn("number", FilmLayerModel::id)
                column("depth (nm)", FilmLayerModel::depthProperty).makeEditable()
                column("fulfill", FilmLayerModel::fulfillProperty).makeEditable()
                column("material", FilmLayerModel::materialProperty).useComboBox(materials)
                    .cellFormat { text = it!!.name }
                column("enabled", FilmLayerModel::enabledProperty).useCheckbox()
            }
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
            button("Run").action { Experiment(layers.map { it.layer() }) }
        }
    }

    private fun load(): MaterialProperties {
        val library = Library()
        val shelves = library.fetch().shelves
        return library.entry(shelves.get(0).content.get(0).content.get(0)).data[0]
    }
}