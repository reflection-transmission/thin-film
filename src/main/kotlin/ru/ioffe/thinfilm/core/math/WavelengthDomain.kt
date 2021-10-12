package ru.ioffe.thinfilm.core.math

class WavelengthDomain(val min: Int, val max: Int) {

    companion object {
        fun default(): WavelengthDomain {
            return WavelengthDomain(400, 1800)
        }

        fun visible(): WavelengthDomain {
            return WavelengthDomain(380, 780)
        }
    }

}