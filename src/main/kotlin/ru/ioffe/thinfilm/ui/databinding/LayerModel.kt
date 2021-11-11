package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.net.MaterialRegistry
import tornadofx.*

class LayerModel(type: Int, depth: Double, fulfill: Double, material: MaterialReference) {

    companion object {
        val Ambient = 1
        val Film = 0
        val Substrate = 2
    }

    val typeProperty = SimpleIntegerProperty(type)
    var type by typeProperty

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val fulfillProperty = SimpleDoubleProperty(fulfill)
    var fulfill by fulfillProperty

    val materialProperty = SimpleObjectProperty(material)
    var material by materialProperty

    fun layer(registry: MaterialRegistry): Layer = Layer(registry.get(material.id).properties, depth, fulfill)

}
