package ru.ioffe.thinfilm.core.util

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.getValue
import tornadofx.setValue

class Reference<V>(private val origin: Registry<V>, id: Int) {

    private val property = SimpleIntegerProperty(id)
    var id by property

    override fun toString(): String = origin.get(this).toString()

    fun value(): V = origin.get(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reference<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}