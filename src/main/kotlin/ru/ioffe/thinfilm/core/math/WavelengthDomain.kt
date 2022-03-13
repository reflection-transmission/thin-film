package ru.ioffe.thinfilm.core.math

/**
 * Represents a wavelength domain
 * @param min minimum value of the domain (in nanometers)
 * @param max maximum value of the domain (in nanometers)
 */
class WavelengthDomain(val min: Int, val max: Int) {

    /**
     * Returns a ready-to-use range of this wavelength domain
     */
    fun range(): IntRange = min..max

    companion object {
        fun default(): WavelengthDomain {
            return WavelengthDomain(200, 2000)
        }

        fun visible(): WavelengthDomain {
            return WavelengthDomain(380, 780)
        }
    }

}