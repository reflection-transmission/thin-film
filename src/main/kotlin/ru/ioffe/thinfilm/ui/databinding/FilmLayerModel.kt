package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import ru.ioffe.thinfilm.core.model.FilmLayer
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.net.MaterialRegistry
import tornadofx.*

class FilmLayerModel(depth: Double, fulfill: Double, material: Int) {

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val fulfillProperty = SimpleDoubleProperty(fulfill)
    var fulfill by fulfillProperty

    val materialProperty = SimpleIntegerProperty(material)
    var material by materialProperty

    fun layer(registry: MaterialRegistry): Layer = FilmLayer(registry.get(material).properties, depth, fulfill)

}
