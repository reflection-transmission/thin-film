package ru.ioffe.thinfilm.core

import ru.ioffe.thinfilm.core.persistence.Persistence
import ru.ioffe.thinfilm.core.util.Session
import kotlin.test.Test

class PersistenceTest {

    @Test
    fun positive() {
        Persistence().save(Session())
    }
}