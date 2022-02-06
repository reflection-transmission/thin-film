package ru.ioffe.thinfilm.core.util

import javafx.collections.ObservableList

class Registry<T>(private val default: T, vararg content: T) {

    private val registry = mutableMapOf<Reference<T>, T>()
    private val subscribers = mutableListOf<ObservableList<Reference<T>>>()

    init {
        content.forEach(this::add)
    }

    fun get(reference: Reference<T>): T {
        return registry[reference] ?: default
    }

    private fun added(item: T): Boolean {
        return registry.containsValue(item)
    }

    fun add(item: T) {
        if (!added(item)) {
            val reference = Reference(this, registry.size)
            registry[reference] = item
            subscribers.forEach { it.add(reference) }
        }
    }

    fun remove(item: T) {
        if (added(item)) {
            val reference = reference(item)
            subscribers.forEach { it.remove(reference) }
            registry.remove(reference)
        }
    }

    private fun reference(item: T) = registry.filterValues { it == item }.keys.first()

    fun subscribe(subscriber: ObservableList<Reference<T>>) {
        subscriber.addAll(registry.keys)
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: ObservableList<Reference<T>>) {
        subscribers.remove(subscriber)
    }

    fun values() : List<T> = registry.values.toList()

}