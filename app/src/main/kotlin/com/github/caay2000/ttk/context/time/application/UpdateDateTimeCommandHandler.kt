package com.github.caay2000.ttk.context.time.application

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.time.domain.service.DateTimeUpdaterService
import java.util.UUID

class UpdateDateTimeCommandHandler(dateTimeProvider: DateTimeProvider) : CommandHandler<UpdateDateTimeCommand> {

    private val dateTimeUpdater = DateTimeUpdaterService(dateTimeProvider)

    override fun invoke(command: UpdateDateTimeCommand) = dateTimeUpdater.invoke()
}

class UpdateDateTimeCommand : DateTimeCommand {
    override val commandId: UUID = UUID.randomUUID()
}
