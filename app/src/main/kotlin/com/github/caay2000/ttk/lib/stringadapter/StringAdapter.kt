package com.github.caay2000.ttk.lib.stringadapter

import com.github.caay2000.ttk.context.shared.domain.Location

class StringAdapter {

    internal fun execute(input: String) = parseRoute(input)

    private fun parseRoute(input: String): List<Location> {
        val regex = "^([AB])+\$".toRegex()
        val values = regex.find(input)!!.value
        return values.asIterable()
            .filterNot { it.isWhitespace() }
            .map { it.toLocation() }
    }

    private fun Char.toLocation(): Location =
        when (this) {
            'A' -> Location.WAREHOUSE_A
            'B' -> Location.WAREHOUSE_B
            else -> throw IllegalArgumentException("Invalid input")
        }
}
