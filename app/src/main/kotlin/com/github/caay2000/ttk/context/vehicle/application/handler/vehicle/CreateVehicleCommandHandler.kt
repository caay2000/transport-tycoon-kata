package com.github.caay2000.ttk.context.vehicle.application.handler.vehicle

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.application.service.VehicleCreatorService
import com.github.caay2000.ttk.context.vehicle.domain.VehicleType
import java.util.UUID

class CreateVehicleCommandHandler(eventPublisher: EventPublisher, worldRepository: WorldRepository, vehicleRepository: VehicleRepository) : CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(eventPublisher, worldRepository, vehicleRepository)

    override fun invoke(command: CreateVehicleCommand) {
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.vehicleId.toDomainId(), VehicleType.valueOf(command.vehicleType), command.stopName)
    }
}

data class CreateVehicleCommand(val worldId: UUID, val vehicleId: UUID, val vehicleType: String, val stopName: String) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
