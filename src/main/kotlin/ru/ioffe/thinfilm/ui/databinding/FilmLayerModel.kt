package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import ru.ioffe.thinfilm.core.model.FilmLayer
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import tornadofx.*

class FilmLayerModel(depth: Double, fulfill: Double, material: Material, enabled: Boolean) {

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val fulfillProperty = SimpleDoubleProperty(fulfill)
    var fulfill by fulfillProperty

    val materialProperty = SimpleObjectProperty(material)
    var material by materialProperty

    val enabledProperty = SimpleBooleanProperty(enabled)
    var enabled by enabledProperty

    fun layer(): Layer = FilmLayer(material.properties, depth, fulfill)

}
