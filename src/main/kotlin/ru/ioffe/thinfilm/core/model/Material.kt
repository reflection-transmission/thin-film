package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.MaterialProperties

data class Material(val name: String, val properties: MaterialProperties = MaterialProperties.TabulatedN("1 1")) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Material

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
