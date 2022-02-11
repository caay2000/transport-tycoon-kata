package com.github.caay2000.ttk.context.time.domain

@JvmInline
value class DateTime(private val time: Int) {

    fun value(): Int = time
}
