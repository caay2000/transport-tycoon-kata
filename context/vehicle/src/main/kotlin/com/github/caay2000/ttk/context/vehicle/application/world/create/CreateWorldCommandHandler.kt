package com.github.caay2000.ttk.context.vehicle.application.world.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class CreateWorldCommandHandler(worldRepository: WorldRepository) : CommandHandler<CreateWorldCommand> {

    private val worldCreatorService = WorldCreatorService(worldRepository)

    override fun invoke(command: CreateWorldCommand) =
        worldCreatorService.invoke(command.worldId.toDomainId())
            .getOrHandle { throw it }
}

data class CreateWorldCommand(val worldId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
