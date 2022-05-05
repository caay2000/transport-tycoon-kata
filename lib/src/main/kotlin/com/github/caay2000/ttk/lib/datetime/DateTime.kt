package com.github.caay2000.ttk.lib.datetime

@JvmInline
value class DateTime(private val time: Int) {

    fun value(): Int = time
}
