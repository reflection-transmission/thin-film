package ru.ioffe.thinfilm.core.math

class WavelengthDomain(val min: Int, val max: Int) {

    companion object {
        fun default(): WavelengthDomain {
            return WavelengthDomain(200, 2000)
        }
    }

}