package ru.ioffe.thinfilm.core.model

import ru.ioffe.thinfilm.net.RefractiveIndex

data class Layer(val depth: Double, val index: RefractiveIndex = RefractiveIndex.Constant(1.0))