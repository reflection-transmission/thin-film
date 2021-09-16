package ru.ioffe.thinfilm.ui.library

import javafx.scene.chart.NumberAxis
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.MaterialProperties
import ru.ioffe.thinfilm.net.Shelf
import tornadofx.*

class LibraryView : View() {

	private val library = Library()
	private val record = library.fetch()
	private val selection = mutableListOf<Material>()

	override val root = hbox {
		val tree = treeview<Any> {
			configure()
		}
		vbox {
			val chart = linechart("Material Data", NumberAxis(), NumberAxis()) {
				createSymbols = false
			}
			tree.selectionModel.selectedItemProperty().addListener(
					javafx.beans.value.ChangeListener { _, _, newValue ->
						chart.data.clear()
						val value = newValue.value
						if (value is Shelf.Book.Page) {
							val material = material(value)
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
					}
			)
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
			if (parent == root) record.shelves
			else if (value is Shelf) value.content
			else if (value is Shelf.Book) value.content
			else null
		}
	}

	private fun material(page: Shelf.Book.Page): MaterialProperties {
		return library.entry(page).data[0]
	}

}