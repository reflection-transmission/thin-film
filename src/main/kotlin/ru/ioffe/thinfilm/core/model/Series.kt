package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
data class Series(val spectrum: Spectrum, val name: String)
