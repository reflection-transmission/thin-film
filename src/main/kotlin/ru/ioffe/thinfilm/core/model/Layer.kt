package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
data class Layer(val type: Int = 0, val depth: Double, val material: Material)