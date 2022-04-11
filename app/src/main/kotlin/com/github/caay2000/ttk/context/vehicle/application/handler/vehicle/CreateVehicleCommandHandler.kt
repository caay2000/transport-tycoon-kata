package com.github.caay2000.ttk.context.vehicle.application.handler.vehicle

import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.application.service.VehicleCreatorService
import com.github.caay2000.ttk.context.vehicle.domain.VehicleType
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class CreateVehicleCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository, vehicleRepository: VehicleRepository) :
    CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(eventPublisher, worldRepository, vehicleRepository)

    override fun invoke(command: CreateVehicleCommand) {
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.vehicleId.toDomainId(), VehicleType.valueOf(command.vehicleType), command.stopName)
    }
}

data class CreateVehicleCommand(val worldId: UUID, val vehicleId: UUID, val vehicleType: String, val stopName: String) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
