package com.github.caay2000.ttk.infrastructure

import com.github.caay2000.ttk.api.inbound.Destination
import com.github.caay2000.ttk.api.inbound.Steps
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi

class StringAdapter(private val application: TransportTycoonApi) {

    internal fun execute(input: String): Steps {

        val route = parseRoute(input)
        return application.calculateSteps(route)
    }

    private fun parseRoute(input: String): List<Destination> {
        val regex = "^([AB])+\$".toRegex()
        val values = regex.find(input)!!.value
        return values.asIterable()
            .filterNot { it.isWhitespace() }
            .map { it.toDestination() }
    }

    private fun Char.toDestination(): Destination =
        when (this) {
            'A' -> Destination.WAREHOUSE_A
            'B' -> Destination.WAREHOUSE_B
            else -> throw IllegalArgumentException("Invalid input")
        }
}


