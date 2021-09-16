package ru.ioffe.thinfilm.core.model

class Spectrum(val ambient: Layer, val transmitted: List<Wavelength>, val reflected: List<Wavelength>) {

    fun apply(layer: Layer): Spectrum = layer.apply(this)

}