package ru.ioffe.thinfilm.core.model

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class ExperimentSeries(
    val spectrum: Spectrum,
    private val name: String,
    val imported: Boolean,
    enabled: Boolean,
    transmission: Boolean,
    reflection: Boolean,
    absorption: Boolean = false
) {

    val enabledProperty = SimpleBooleanProperty(true)
    var enabled by enabledProperty

    val transmissionProperty = SimpleBooleanProperty(transmission)
    var transmission by transmissionProperty

    val reflectionProperty = SimpleBooleanProperty(reflection)
    var reflection by reflectionProperty

    val absorptionProperty = SimpleBooleanProperty(absorption)
    var absorption by absorptionProperty

    override fun toString(): String {
        return name
    }

}