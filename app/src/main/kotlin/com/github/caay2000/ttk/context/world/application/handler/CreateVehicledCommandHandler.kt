package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.VehicleCreatorService
import java.util.UUID

class CreateVehicleCommandHandler(worldRepository: WorldRepository) : CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(worldRepository)

    override fun invoke(command: CreateVehicleCommand) {
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.vehicleId.toDomainId(), command.vehicleType, command.vehicleStatus)
    }
}

data class CreateVehicleCommand(override val worldId: UUID, val vehicleId: UUID, val vehicleType: String, val vehicleStatus: String) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
