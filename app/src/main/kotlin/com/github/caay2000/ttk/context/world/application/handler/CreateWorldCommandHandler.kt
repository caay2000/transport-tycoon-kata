package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.WorldConfiguratorService
import java.util.UUID

class CreateWorldCommandHandler(eventPublisher: EventPublisher, worldRepository: WorldRepository) : CommandHandler<CreateWorldCommand> {

    private val worldConfigurator = WorldConfiguratorService(eventPublisher, worldRepository)

    override fun invoke(command: CreateWorldCommand) {
        worldConfigurator.invoke(command.worldId.toDomainId())
    }
}

data class CreateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
