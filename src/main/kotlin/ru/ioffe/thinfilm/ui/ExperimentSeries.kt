package ru.ioffe.thinfilm.ui

import javafx.beans.property.SimpleBooleanProperty
import ru.ioffe.thinfilm.core.model.Series
import ru.ioffe.thinfilm.core.model.Spectrum
import tornadofx.*

class ExperimentSeries(
    val series: Series,
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
        return series.name
    }

}