package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
data class Layer(val depth: Double, val material: Material)