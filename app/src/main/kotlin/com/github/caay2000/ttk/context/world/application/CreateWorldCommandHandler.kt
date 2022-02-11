package com.github.caay2000.ttk.context.world.application

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.service.WorldConfiguratorService
import java.util.UUID

class CreateWorldCommandHandler(worldRepository: WorldRepository) : CommandHandler<CreateWorldCommand> {

    private val worldConfigurator = WorldConfiguratorService(worldRepository)

    override fun invoke(command: CreateWorldCommand) {
        worldConfigurator.create(command.worldId.toDomainId())
    }
}

data class CreateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
