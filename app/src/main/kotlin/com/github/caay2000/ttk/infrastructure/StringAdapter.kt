package com.github.caay2000.ttk.infrastructure

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.domain.Location

class StringAdapter(private val application: TransportTycoonApi) {

    internal fun execute(input: String): Int {

        val destinations = parseRoute(input)
        return application.execute(destinations)
    }

    private fun parseRoute(input: String): List<Cargo> {
        val regex = "^([AB])+\$".toRegex()
        val values = regex.find(input)!!.value
        return values.asIterable()
            .filterNot { it.isWhitespace() }
            .map { it.toDelivery() }
    }

    private fun Char.toDelivery(): Cargo =
        when (this) {
            'A' -> Cargo(Location.WAREHOUSE_A)
            'B' -> Cargo(Location.WAREHOUSE_B)
            else -> throw IllegalArgumentException("Invalid input")
        }
}
