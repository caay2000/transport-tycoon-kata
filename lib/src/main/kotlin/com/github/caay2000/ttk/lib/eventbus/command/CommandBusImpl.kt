package com.github.caay2000.ttk.lib.eventbus.command

import com.github.caay2000.ttk.lib.eventbus.impl.KTCommandPublisher

class CommandBusImpl : CommandBus<Command> {

    private val commandPublisher = KTCommandPublisher<Command>()
    override fun publish(command: Command) {
        println(command)
        commandPublisher.publish(command)
    }
}
