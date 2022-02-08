package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Shelf(@SerialName("SHELF") val shelf: String, val name: String, val content: List<Book>, val info: String = "") {

    override fun toString(): String {
        return name
    }

    @Serializable
    data class Book(@SerialName("BOOK") val book: String, var name: String, val info: String, val content: List<Page>) {

        init {
            name = name.replace("<sub>", "").replace("</sub>", "")
        }

        override fun toString(): String {
            return name
        }

        @Serializable
        data class Page(
            @SerialName("PAGE") val page: String,
            val name: String,
            val data: String,
            val info: String = ""
        ) {

            var book: String = ""

            override fun toString(): String {
                return "$book $name"
            }

        }
    }

}
