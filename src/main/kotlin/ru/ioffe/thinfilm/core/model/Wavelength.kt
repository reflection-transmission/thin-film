package ru.ioffe.thinfilm.core.model

data class Wavelength(
    val length: Double,
    val angle: Double,
    val transmitted: Double,
    val reflected: Double,
    val polarization: Polarization = Polarization.NonPolar
) {
    fun absorbed(): Double = 1.0 - (transmitted + reflected)
}