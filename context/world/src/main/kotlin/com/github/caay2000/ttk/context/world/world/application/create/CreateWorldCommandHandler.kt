package com.github.caay2000.ttk.context.world.world.application.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.WorldCommand
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class CreateWorldCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository) :
    CommandHandler<CreateWorldCommand> {

    private val worldCreatorService = WorldCreatorService(eventPublisher, worldRepository)

    override fun invoke(command: CreateWorldCommand) =
        worldCreatorService.invoke(command.worldId.toDomainId())
            .getOrHandle { throw it }
}

data class CreateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
