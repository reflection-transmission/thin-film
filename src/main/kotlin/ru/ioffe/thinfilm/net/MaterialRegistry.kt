package ru.ioffe.thinfilm.net

import ru.ioffe.thinfilm.core.model.Material

class MaterialRegistry {

    private val registry = mutableListOf<Material>()

    fun add(material: Material) {
        if (!added(material))
            registry.add(material)
    }

    fun remove(material: Material) {
        if (added(material))
            registry.remove(material)
    }

    fun materials(): List<Material> {
        return registry.toList()
    }

    fun get(index: Int): Material {
        return materials()[index]
    }

    fun indexes(): List<Int> {
        return IntArray(registry.size) { it }.asList()
    }

    fun added(material: Material): Boolean {
        return registry.contains(material)
    }
}