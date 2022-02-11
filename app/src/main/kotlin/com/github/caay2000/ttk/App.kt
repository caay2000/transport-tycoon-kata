package com.github.caay2000.ttk

import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.application.ApplicationConfiguration
import com.github.caay2000.ttk.lib.stringadapter.StringAdapter

class App {

    private val configuration = ApplicationConfiguration()

    private val application = Application(
        configuration.commandBus,
        configuration.eventRepository,
        configuration.worldRepository,
        configuration.dateTimeProvider
    )
    private val adapter = StringAdapter()

    fun invoke(input: String) = application.execute(adapter.execute(input))
}

fun main(args: Array<String>) {
    println(App().invoke(args[0]).duration)
}
