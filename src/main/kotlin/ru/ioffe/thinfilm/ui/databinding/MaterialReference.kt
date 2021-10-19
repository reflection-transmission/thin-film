package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.SimpleIntegerProperty
import ru.ioffe.thinfilm.net.MaterialRegistry
import tornadofx.*

class MaterialReference(private val origin: MaterialRegistry, id: Int) {

    val property = SimpleIntegerProperty(id)
    var id by property

    override fun toString(): String {
        return origin.get(id).name
    }

}