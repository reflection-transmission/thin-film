package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.RefractiveIndexData

data class Layer(val depth: Double, val index: RefractiveIndex = RefractiveIndexData.Constant(1.0).index())