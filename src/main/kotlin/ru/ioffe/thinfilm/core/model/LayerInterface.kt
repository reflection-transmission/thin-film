package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.MaterialProperties

data class LayerInterface(val first: MaterialProperties, val second: MaterialProperties, val theta: Double)