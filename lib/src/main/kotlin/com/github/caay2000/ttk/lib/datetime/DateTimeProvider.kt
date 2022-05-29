package com.github.caay2000.ttk.lib.datetime

interface DateTimeProvider {

    fun now(): DateTime
    fun inc()
    fun hash(): String
}
