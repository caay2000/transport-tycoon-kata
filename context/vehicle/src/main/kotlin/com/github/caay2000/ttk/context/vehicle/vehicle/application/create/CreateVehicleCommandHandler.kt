package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.shared.domain.toVehicleType
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class CreateVehicleCommandHandler(vehicleRepository: VehicleRepository, stopRepository: StopRepository) :
    CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(vehicleRepository, stopRepository)

    override fun invoke(command: CreateVehicleCommand) {
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.vehicleId.toDomainId(), command.vehicleType.toVehicleType())
            .getOrHandle { throw it }
    }
}

data class CreateVehicleCommand(val worldId: UUID, val stopId: UUID, val vehicleId: UUID, val vehicleType: String) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
