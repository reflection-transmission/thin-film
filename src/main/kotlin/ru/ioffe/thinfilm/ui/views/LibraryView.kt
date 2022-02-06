package ru.ioffe.thinfilm.ui.views

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Button
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.paint.Color
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.util.Reference
import ru.ioffe.thinfilm.core.util.ThinFilmContext
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import ru.ioffe.thinfilm.net.Shelf
import tornadofx.*

class LibraryView(private val context: ThinFilmContext) : View() {

    private val library = Library()
    private val record = library.fetch()
    private val selected = FXCollections.observableArrayList<Reference<Material>>()

    init {
        title = "RefractiveIndex.info Library"
        context.materials().subscribe(selected)
        selected.removeIf { context.materials().get(it).properties() is MaterialProperties.Constant }
    }

    override fun onUndock() {
        super.onUndock()
        context.materials().subscribe(selected)
    }

    override val root = hbox {
        style {
            padding = box(5.px)
        }
        val tree = treeview<Any> {
            configure()
        }
        lateinit var add: Button
        lateinit var chart: LineChart<Number, Number>
        vbox {
            style {
                padding = box(0.px, 5.px)
            }
            chart = linechart("Material Data", NumberAxis(), NumberAxis()) {
                createSymbols = false
            }
            hbox {
                alignment = Pos.BASELINE_RIGHT
                add = this.button("Add")
            }
        }
        val list = listview(selected) {
            cellFormat {
                val item: Material = context.materials().get(this@cellFormat.item)
                graphic = hbox(spacing = 5) {
                    alignment = Pos.BASELINE_LEFT
                    label(item.name)
                    button("❌") {
                        style {
                            backgroundColor += Color.TRANSPARENT
                        }
                        action {
                            context.materials().remove(item)
                        }
                    }
                }
            }
        }
        list.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            draw(chart, newValue, add)
        }
        tree.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            draw(chart, newValue.value, add)
        }
    }

    private fun draw(chart: LineChart<Number, Number>, value: Any?, add: Button) {
        chart.data.clear()
        lateinit var material: MaterialProperties
        if (value is Shelf.Book.Page) {
            material = material(value)
            val entry = Material.Defined(value.name, material)
            add.action {
                context.materials().add(entry)
            }
        } else if (value is Reference<*>) {
            material = context.materials().get(value as Reference<Material>).properties()
        } else {
            return
        }
        chart.series("n") {
            material.wavelengths().forEach {
                data(it, material.n(it))
            }
        }
        chart.series("k") {
            material.wavelengths().forEach {
                data(it, material.k(it))
            }
        }
    }

    private fun TreeView<Any>.configure() {
        root = TreeItem("Library")
        cellFormat {
            text = when (it) {
                is String -> it
                is Shelf -> it.name
                is Shelf.Book -> it.name
                is Shelf.Book.Page -> it.name
                else -> throw IllegalArgumentException("Incorrect type for tree viewer")
            }
        }
        populate { parent ->
            val value = parent.value
            if (parent == root) record
            else if (value is Shelf) value.content
            else if (value is Shelf.Book) value.content
            else null
        }
    }

    private fun material(page: Shelf.Book.Page): MaterialProperties {
        return library.entry(page).data[0]
    }

}