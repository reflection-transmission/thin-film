package ru.ioffe.thinfilm

import ru.ioffe.thinfilm.ui.Application
import tornadofx.*

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<Application>(args)
        }
    }

}