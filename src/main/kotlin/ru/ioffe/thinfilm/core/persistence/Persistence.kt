package ru.ioffe.thinfilm.core.persistence

import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.util.Session
import ru.ioffe.thinfilm.net.RefractiveIndexData

class Persistence {

    fun save(session: Session) {
        val material = Material("material", RefractiveIndexData.Constant(1.5).index())
    }

}