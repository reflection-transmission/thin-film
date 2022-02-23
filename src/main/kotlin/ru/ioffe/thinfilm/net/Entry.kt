package ru.ioffe.thinfilm.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entry(
    @SerialName("REFERENCES") val references: String,
    @SerialName("COMMENTS") val comments: String = "",
    @SerialName("DATA") val data: List<RefractiveIndex>,
    @SerialName("SPECS") val specs: Map<String, String> = mutableMapOf()
)
