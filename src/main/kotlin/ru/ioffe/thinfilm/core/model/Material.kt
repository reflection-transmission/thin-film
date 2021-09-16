package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.MaterialProperties

data class Material(val name: String, val properties : MaterialProperties = MaterialProperties.TabulatedN("1 1"))
