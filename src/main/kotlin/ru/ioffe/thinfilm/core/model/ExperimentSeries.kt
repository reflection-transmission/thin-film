package ru.ioffe.thinfilm.core.model

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class ExperimentSeries(val spectrum: Spectrum, private val name: String, enabled: Boolean, val imported: Boolean) {

    val enabledProperty = SimpleBooleanProperty(true)
    var enabled by enabledProperty

    override fun toString(): String {
        return name
    }

}