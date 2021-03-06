package ru.ioffe.thinfilm.core.model

@kotlinx.serialization.Serializable
data class Spectrum(val ambient: Layer, val wavelengths: List<Wavelength>)