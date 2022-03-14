package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
class Material(val name: String, val dispersion: RefractiveIndex) {

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

}