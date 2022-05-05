package com.github.caay2000.ttk.context.world.time.application

import com.github.caay2000.ttk.context.world.WorldCommand
import com.github.caay2000.ttk.lib.datetime.DateTimeProvider
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class UpdateDateTimeCommandHandler(dateTimeProvider: DateTimeProvider) : CommandHandler<UpdateDateTimeCommand> {

    private val dateTimeUpdater = DateTimeUpdaterService(dateTimeProvider)

    override fun invoke(command: UpdateDateTimeCommand) = dateTimeUpdater.invoke()
}

data class UpdateDateTimeCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
