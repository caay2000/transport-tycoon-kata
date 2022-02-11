package com.github.caay2000.ttk.context.core.command

interface CommandBus<in COMMAND : Command> {

    fun publish(command: COMMAND)
}
