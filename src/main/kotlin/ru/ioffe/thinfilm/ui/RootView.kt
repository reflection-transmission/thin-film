package ru.ioffe.thinfilm.ui

import javafx.scene.input.KeyCombination
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.library.LibraryView
import tornadofx.*

class RootView : View() {

    private val registry = MaterialRegistry()

    override val root = vbox {
        menubar {
            menu("File") {
                item("New", KeyCombination.valueOf("Ctrl+N")).action { println("new") }
                item("Save", KeyCombination.valueOf("Ctrl+S")).action { println("saved") }
                item("Open", KeyCombination.valueOf("Ctrl+O")).action { println("opened") }
            }
            menu("Library") {
                item("Materials", KeyCombination.valueOf("Ctrl+L")).action { openInternalWindow(LibraryView(registry)) }
            }
            menu("Help") {
                item("About") { println("about") }
            }
        }
        borderpane {
            top(Workbench(registry).root)
        }
    }


}