package com.github.caay2000.ttk.lib.eventbus.command

interface CommandBus<in COMMAND : Command> {

    fun publish(command: COMMAND)
}
