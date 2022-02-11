package com.github.caay2000.ttk.context.time.application

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.time.domain.service.DateTimeUpdaterService
import java.util.UUID

class UpdateDateTimeCommandHandler(private val dateTimeUpdater: DateTimeUpdaterService) : CommandHandler<UpdateDateTimeCommand> {

    override fun invoke(command: UpdateDateTimeCommand) = dateTimeUpdater.invoke()
}

class UpdateDateTimeCommand : DateTimeCommand {
    override val commandId: UUID = UUID.randomUUID()
}
