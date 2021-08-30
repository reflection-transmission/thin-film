package ru.ioffe.thinfilm.ui

import javafx.scene.input.KeyCombination
import tornadofx.*

class RootView : View() {

    override val root = vbox {
        menubar {
            menu("File") {
                item("New", KeyCombination.valueOf("Ctrl+N")).action { println("new") }
                item("Save", KeyCombination.valueOf("Ctrl+S")).action { println("saved") }
                item("Open", KeyCombination.valueOf("Ctrl+O")).action { println("opened") }
            }
            menu("Library") {
                item("Materials", KeyCombination.valueOf("Ctrl+L")).action { openInternalWindow<LibraryView>() }
            }
            menu("Help") {
                item("About") { println("about") }
            }
        }
        borderpane {
            center<Workbench>()
        }
    }


}