package ru.ioffe.thinfilm.core

import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.Layer

class Experiment(val layers: List<Layer>, val wavelengths: WavelengthDomain) {

    fun start(): Result {
        return Result()
    }

}