package com.github.caay2000.ttk.context.world.application.time

import com.github.caay2000.ttk.lib.datetime.DateTimeProvider

class DateTimeUpdaterService(private val dateTimeProvider: DateTimeProvider) {

    fun invoke() = dateTimeProvider.inc().also {
        println("Time Increased to ${this.dateTimeProvider.now()}")
    }
}
