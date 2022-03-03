package ru.ioffe.thinfilm.core.math

class WavelengthDomain(val min: Int, val max: Int) {

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