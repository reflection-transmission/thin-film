package ru.ioffe.thinfilm.core.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class Layer(id: Int, depth: Double, fulfill: Double, material: Material, enabled: Boolean) {
    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val fulfillProperty = SimpleDoubleProperty(fulfill)
    var fulfill by fulfillProperty

    val materialProperty = SimpleObjectProperty(material)
    var material by materialProperty

    val enabledProperty = SimpleBooleanProperty(enabled)
    var enabled by enabledProperty


}
