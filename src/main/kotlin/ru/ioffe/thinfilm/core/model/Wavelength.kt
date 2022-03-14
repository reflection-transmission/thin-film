package ru.ioffe.thinfilm.core.model

/**
 * An object that stores the current state of monochromatic light ray. All properties are immutable,
 * thus on every change you have to create a new instance
 */
@kotlinx.serialization.Serializable
data class Wavelength(
    val length: Double,
    val angle: Double,
    val transmitted: Double,
    val reflected: Double,
    val polarization: Polarization = Polarization.NonPolar
) {
    fun absorbed(): Double = 1.0 - (transmitted + reflected)

    /**
     * Creates new instance of the current wavelength. All the properties can be changed or not:
     * @sample
     * `val source = Wavelength(1.0, 0.0, 1.0, 0.0, Polarization.NonPolar)`
     * `val theSame = source.instance() // For the same`
     * `val changedPolarization = source.instance(polarization = Polarization.Linear)`
     * `val changedTrRe = source.instance(transmitted = source.transmitted - 0.3, reflected = source.reflected + 0.2)`
     */
    fun instance(
        length: Double = this.length,
        angle: Double = this.angle,
        transmitted: Double = this.transmitted,
        reflected: Double = this.reflected,
        polarization: Polarization = this.polarization
    ): Wavelength = Wavelength(length, angle, transmitted, reflected, polarization)
}