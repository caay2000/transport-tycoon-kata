package com.github.caay2000.ttk.context.vehicle.vehicle.application.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.shared.domain.toVehicleType
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus
import java.util.UUID

class CreateVehicleCommandHandler(
    queryBus: QueryBus,
    vehicleRepository: VehicleRepository
) :
    CommandHandler<CreateVehicleCommand> {

    private val vehicleCreatorService = VehicleCreatorService(queryBus, vehicleRepository)

    override fun invoke(command: CreateVehicleCommand) {
        vehicleCreatorService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.vehicleId.toDomainId(), command.vehicleType.toVehicleType())
            .getOrHandle { throw it }
    }
}

data class CreateVehicleCommand(val worldId: UUID, val stopId: UUID, val vehicleId: UUID, val vehicleType: String) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
