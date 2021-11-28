package com.github.caay2000.ttk.infrastructure

import com.github.caay2000.ttk.api.inbound.Delivery
import com.github.caay2000.ttk.api.inbound.Steps
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi

class StringAdapter(private val application: TransportTycoonApi) {

    internal fun execute(input: String): Steps {

        val destinations = parseRoute(input)
        return application.execute(destinations)
    }

    private fun parseRoute(input: String): List<Delivery> {
        val regex = "^([AB])+\$".toRegex()
        val values = regex.find(input)!!.value
        return values.asIterable()
            .filterNot { it.isWhitespace() }
            .map { it.toDelivery() }
    }

    private fun Char.toDelivery(): Delivery =
        when (this) {
            'A' -> Delivery.WAREHOUSE_A
            'B' -> Delivery.WAREHOUSE_B
            else -> throw IllegalArgumentException("Invalid input")
        }
}
