package ru.ioffe.thinfilm.ui.databinding

import javafx.beans.property.*
import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.util.Reference
import ru.ioffe.thinfilm.core.util.Registry
import tornadofx.*

class LayerModel(
    type: Int,
    depth: Double,
    fulfill: Double,
    material: Reference<Material>,
    enabled: Boolean = true
) {

    companion object {
        const val Ambient = 1
        const val Film = 0
        const val Substrate = 2
    }

    val typeProperty = SimpleIntegerProperty(type)
    var type by typeProperty

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val fulfillProperty = SimpleDoubleProperty(fulfill)
    var fulfill by fulfillProperty

    val materialProperty = SimpleObjectProperty(material)
    var material by materialProperty

    val enabledProperty = SimpleBooleanProperty(enabled)
    var enabled by enabledProperty

    fun layer(registry: Registry<Material>): Layer =
        Layer(registry.get(material).properties(), depth, enabled, fulfill)

}
