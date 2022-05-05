package com.github.caay2000.ttk.lib.eventbus.command

interface CommandHandler<in COMMAND : Command> {

    fun invoke(command: COMMAND)
}
