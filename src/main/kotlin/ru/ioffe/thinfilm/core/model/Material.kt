package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.RefractiveIndex

sealed class Material(val name: String) {

    abstract fun properties(): RefractiveIndex

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Material

        if (name != other.name) return false

        return true
    }

    override fun toString(): String = name

    override fun hashCode(): Int {
        return name.hashCode()
    }

    class Defined(name: String, private val properties: RefractiveIndex) : Material(name) {

        override fun properties(): RefractiveIndex = properties

    }

}