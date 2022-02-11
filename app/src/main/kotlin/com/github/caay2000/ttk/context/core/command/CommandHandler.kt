package com.github.caay2000.ttk.context.core.command

interface CommandHandler<in COMMAND : Command> {

    fun invoke(command: COMMAND)
}
