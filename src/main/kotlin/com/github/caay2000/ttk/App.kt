package com.github.caay2000.ttk

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.application.configuration.ApplicationConfiguration

class App {

    private val application = Application(ApplicationConfiguration())
    private val adapter = LocationAdapter()

    fun invoke(input: String): Application.Result {
        application.create()
        return application.execute(adapter.execute(input))
    }

    private class LocationAdapter {

        fun execute(input: String) = parseRoute(input)

        private fun parseRoute(input: String): List<String> {
            val regex = "^([AB])+\$".toRegex()
            val values = regex.find(input)!!.value
            return values.asIterable()
                .filterNot { it.isWhitespace() }
                .map { it.toLocation() }
        }

        private fun Char.toLocation(): String =
            when (this) {
                'A' -> "WAREHOUSE_A"
                'B' -> "WAREHOUSE_B"
                else -> throw IllegalArgumentException("Invalid input")
            }
    }
}

fun main(args: Array<String>) {
    println(App().invoke(args[0]).duration)
}
