package ru.ioffe.thinfilm.net

import java.time.LocalDateTime

data class Record(private val date: LocalDateTime, val shelves: List<Shelf>)