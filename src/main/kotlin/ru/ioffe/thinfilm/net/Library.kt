package ru.ioffe.thinfilm.net

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import java.net.URL
import java.time.LocalDateTime
import java.util.stream.Collectors

class Library {

    fun fetch(): Record {
        val shelves = load("library.yml")
            .clean()
            .parse(ListSerializer(Shelf.serializer()))
        return Record(LocalDateTime.now(), shelves)
    }

    fun entry(page: Shelf.Book.Page): Entry {
        return load("data/".plus(page.data).replace(" ", "%20")).parse(Entry.serializer())
    }

    private fun load(file: String): String {
        return URL(
            "https",
            "raw.githubusercontent.com",
            "/polyanskiy/refractiveindex.info-database/master/database/$file"
        ).readText()
    }

    private fun String.clean(): String {
        return this.lines().stream() //
            .filter { it.indexOf("- DIVIDER:") == -1 } //
            .collect(Collectors.toList()) //
            .joinToString(separator = "\n")
    }

    private fun <T> String.parse(deserializer: KSerializer<T>): T {
        val yaml = Yaml(configuration = YamlConfiguration().copy(polymorphismStyle = PolymorphismStyle.Property))
        return yaml.decodeFromString(deserializer, this)
    }

}