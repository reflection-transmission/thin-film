package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
sealed class Polarization {
    object NonPolar : Polarization()
    object Parallel : Polarization()
    object Normal : Polarization()
}
