package ru.ioffe.thinfilm.ui.library

import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import ru.ioffe.thinfilm.net.Library
import ru.ioffe.thinfilm.net.Shelf
import tornadofx.*

class Explorer(library: Library) : TreeView<Any>() {

    private val record = library.fetch()

    init {
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
}