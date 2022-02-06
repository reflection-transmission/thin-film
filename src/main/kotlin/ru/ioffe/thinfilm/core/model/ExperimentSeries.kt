package ru.ioffe.thinfilm.core.model

class ExperimentSeries(val spectrum: Spectrum, private val name: String, var enabled: Boolean, val imported: Boolean) {

    override fun toString(): String {
        return name
    }

}