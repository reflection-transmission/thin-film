package ru.ioffe.thinfilm.net

import javafx.collections.ObservableList
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.ui.databinding.MaterialReference

class MaterialRegistry {

    private val registry = mutableListOf<Material>()
    private val subscribers = mutableListOf<ObservableList<MaterialReference>>()

    init {
        add(Material("Air", MaterialProperties.Constant(1.0)))
        add(Material("Constant 2.3", MaterialProperties.Constant(2.3)))
    }

    fun add(material: Material) {
        if (!added(material)) {
            registry.add(material)
            subscribers.forEach { it.add(MaterialReference(this, registry.indexOf(material))) }
        }
    }

    fun remove(material: Material) {
        if (added(material)) {
            subscribers.forEach { it.remove(registry.indexOf(material), registry.indexOf(material)) }
            registry.remove(material)
        }
    }

    fun materials(): List<Material> {
        return registry.toList()
    }

    fun get(index: Int): Material {
        return materials()[index]
    }

    fun subscribe(subscriber: ObservableList<MaterialReference>) {
        subscriber.addAll(materials().map { MaterialReference(this, registry.indexOf(it)) })
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: ObservableList<MaterialReference>) {
        subscribers.remove(subscriber)
    }

    fun added(material: Material): Boolean {
        return registry.contains(material)
    }

}