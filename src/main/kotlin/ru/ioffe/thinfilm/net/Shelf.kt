package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Shelf(@SerialName("SHELF") val shelf: String, val name: String, val content: List<Book>, val info: String = "") {

    override fun toString(): String {
        return name
    }

    @Serializable
    data class Book(@SerialName("BOOK") val book: String, val name: String, val info: String, val content: List<Page>) {

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

            override fun toString(): String {
                return name
            }

        }
    }

}
