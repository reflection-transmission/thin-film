package ru.ioffe.thinfilm.core.persistence

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.ioffe.thinfilm.core.util.Session
import java.io.File

class Persistence {

    fun save(path: File, session: Session) {
        path.writeText(Json.encodeToString(Dump.serializer(), Dump.from(session)))
    }

    fun load(path: File): Session {
        return Json.decodeFromString<Dump>(path.readText()).session()
    }

}