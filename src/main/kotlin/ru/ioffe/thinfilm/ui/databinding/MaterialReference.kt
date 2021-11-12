package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.SimpleIntegerProperty
import ru.ioffe.thinfilm.net.MaterialRegistry
import tornadofx.*

class MaterialReference(private val origin: MaterialRegistry, id: Int) {

    private val property = SimpleIntegerProperty(id)
    var id by property

    override fun toString(): String {
        return origin.get(this).name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MaterialReference

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}