package ru.ioffe.thinfilm.ui

import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import tornadofx.*

class Workbench() : View() {

    private val layers = listOf(
        Layer(1, 1.0, 1.0, Material("ITO250"), true),
        Layer(2, 1.0, 1.0, Material("ITO250"), true),
        Layer(3, 1.0, 1.0, Material("ITO250"), true),
        Layer(4, 1.0, 1.0, Material("ITO250"), true),
        Layer(5, 1.0, 1.0, Material("ITO250"), true)
    ).asObservable()

    private val materials = listOf(
        Material("ITO250"),
        Material("Glass"),
        Material("Al2O3"),
        Material("Blood")
    ).asObservable()

    override val root = gridpane {
        row {
            button("Run")
        }
        row {
            tableview(layers) {
                gridpaneColumnConstraints {
                    percentWidth = 50.0
                }
                useMaxWidth = true
                isEditable = true
                readonlyColumn("number", Layer::id)
                column("depth (nm)", Layer::depthProperty).makeEditable()
                column("fulfill", Layer::fulfillProperty).makeEditable()
                column("material", Layer::materialProperty).useComboBox(materials).cellFormat { text = it!!.name }
                column("enabled", Layer::enabledProperty).useCheckbox()
            }
            linechart("Material n and k", NumberAxis(), NumberAxis()) {
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
        }
    }

    private fun load(): MaterialProperties {
        val library = Library()
        val shelves = library.fetch().shelves
        return library.entry(shelves.get(0).content.get(0).content.get(0)).data[0]
    }
}