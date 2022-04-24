package com.github.caay2000.ttk.lib.stringadapter

class StringAdapter {

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
