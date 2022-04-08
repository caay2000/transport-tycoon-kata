package com.github.caay2000.ttk.context.vehicle.application.handler.world

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.application.service.WorldCreatorService
import java.util.UUID

class CreateWorldCommandHandler(worldRepository: WorldRepository) : CommandHandler<CreateWorldCommand> {

    private val worldCreatorService = WorldCreatorService(worldRepository)

    override fun invoke(command: CreateWorldCommand) {
        worldCreatorService.invoke(command.worldId.toDomainId())
    }
}

data class CreateWorldCommand(val worldId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
