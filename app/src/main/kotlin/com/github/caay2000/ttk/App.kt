package com.github.caay2000.ttk

import com.github.caay2000.ttk.api.inbound.Steps
import com.github.caay2000.ttk.api.inbound.TransportTycoonApi
import com.github.caay2000.ttk.application.Application
import com.github.caay2000.ttk.infrastructure.StringAdapter

class App {
    private val application: TransportTycoonApi = Application()
    private val adapter = StringAdapter(application)

    fun invoke(input: String): Steps = adapter.execute(input)
}

fun main(args: Array<String>) {
    println(App().invoke(args[0]))
}
