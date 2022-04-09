package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.VehicleUpdaterService
import java.util.UUID

class UpdateVehicleCommandHandler(eventPublisher: EventPublisher, worldRepository: WorldRepository) : CommandHandler<UpdateVehicleCommand> {

    private val vehicleUpdaterService = VehicleUpdaterService(eventPublisher, worldRepository)

    override fun invoke(command: UpdateVehicleCommand) {
        vehicleUpdaterService.invoke(command.worldId.toDomainId(), command.vehicleId.toDomainId(), command.cargoId?.toDomainId(), command.status)
    }
}

data class UpdateVehicleCommand(override val worldId: UUID, val vehicleId: UUID, val cargoId: UUID?, val status: String) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
