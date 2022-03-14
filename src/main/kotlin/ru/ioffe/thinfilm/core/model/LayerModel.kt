package ru.ioffe.thinfilm.core.model

import javafx.beans.property.*
import ru.ioffe.thinfilm.core.util.Reference
import ru.ioffe.thinfilm.core.util.Registry
import tornadofx.*

class LayerModel(
    type: Int,
    depth: Double,
    material: Reference<Material>,
) {

    companion object {
        const val Ambient = 1
        const val Film = 0
        const val Substrate = 2
        fun create(layer: Layer, registry: Registry<Material>): LayerModel {
            return LayerModel(layer.type, layer.depth, registry.get(layer.material))
        }
    }

    val typeProperty = SimpleIntegerProperty(type)
    var type by typeProperty

    val depthProperty = SimpleDoubleProperty(depth)
    var depth by depthProperty

    val materialProperty = SimpleObjectProperty(material)
    var material by materialProperty

    fun layer(registry: Registry<Material>): Layer =
        Layer(type, depth, registry.get(material))

}
