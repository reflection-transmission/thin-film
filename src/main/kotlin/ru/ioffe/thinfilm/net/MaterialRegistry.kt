package ru.ioffe.thinfilm.net

import javafx.collections.ObservableList
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.ui.databinding.MaterialReference

class MaterialRegistry {

    private val registry = mutableMapOf<MaterialReference, Material>()
    private val subscribers = mutableListOf<ObservableList<MaterialReference>>()

    init {
        add(Material("Air", MaterialProperties.Constant(1.0)))
        add(Material("Constant 2.3", MaterialProperties.Constant(2.3)))
        add(Material("Constant 1.5", MaterialProperties.Constant(1.5)))
    }

    fun add(material: Material) {
        if (!added(material)) {
            val reference = MaterialReference(this, registry.size)
            registry[reference] = material
            subscribers.forEach { it.add(reference) }
        }
    }

    fun remove(material: Material) {
        if (added(material)) {
            val reference = reference(material)
            subscribers.forEach { it.remove(reference) }
            registry.remove(reference)
        }
    }

    fun get(reference: MaterialReference): Material {
        return registry[reference] ?: Material("Air", MaterialProperties.Constant(1.0))
    }

    fun subscribe(subscriber: ObservableList<MaterialReference>) {
        subscriber.addAll(registry.keys)
        subscribers.add(subscriber)
    }

    private fun reference(material: Material) = registry.filterValues { it == material }.keys.first()

    fun unsubscribe(subscriber: ObservableList<MaterialReference>) {
        subscribers.remove(subscriber)
    }

    private fun added(material: Material): Boolean {
        return registry.containsValue(material)
    }

}