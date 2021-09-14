package ru.ioffe.thinfilm.core.model

sealed class Polarization {
    object NonPolar : Polarization()
    object Parallel : Polarization()
    object Normal : Polarization()
}
