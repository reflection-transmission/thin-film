package ru.ioffe.thinfilm.core.model

data class Wavelength(
    val length: Double,
    val angle: Double,
    val intensity: Double,
    val polarization: Polarization = Polarization.NonPolar
)