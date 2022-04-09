package com.github.caay2000.ttk.context.vehicle.application.handler.world

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.application.service.StopCreatorService
import java.util.UUID

class CreateStopCommandHandler(worldRepository: WorldRepository) : CommandHandler<CreateStopCommand> {

    private val stopCreatorService = StopCreatorService(worldRepository)

    override fun invoke(command: CreateStopCommand) {
        stopCreatorService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.stopName)
            .getOrHandle { throw it }
    }
}

data class CreateStopCommand(val worldId: UUID, val stopId: UUID, val stopName: String) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
