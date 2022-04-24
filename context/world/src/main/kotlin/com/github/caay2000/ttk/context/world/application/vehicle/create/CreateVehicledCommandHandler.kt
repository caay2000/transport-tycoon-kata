package com.github.caay2000.ttk.context.world.application.vehicle.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.shared.domain.toVehicleType
import com.github.caay2000.ttk.context.world.application.WorldCommand
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class CreateVehicleCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository) : CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(eventPublisher, worldRepository)

    override fun invoke(command: CreateVehicleCommand) =
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.vehicleId.toDomainId(), command.vehicleType.toVehicleType())
            .getOrHandle { throw it }
}

data class CreateVehicleCommand(override val worldId: UUID, val stopId: UUID, val vehicleId: UUID, val vehicleType: String) :
    WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
