package ru.ioffe.thinfilm.ui

import javafx.scene.input.KeyCombination
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.library.LibraryView
import tornadofx.*

class RootView : View() {

    private val registry = MaterialRegistry()

    override val root = vbox {
        menubar {
            menu("Library") {
                item("Materials", KeyCombination.valueOf("Ctrl+L")).action { openInternalWindow(LibraryView(registry)) }
            }
        }
        borderpane {
            top(Workbench(registry).root)
        }
    }


}