package com.github.caay2000.ttk.context.vehicle.application.world.stop.connection.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class CreateConnectionCommandHandler(stopRepository: StopRepository) :
    CommandHandler<CreateConnectionCommand> {

    private val connectionCreatorService = ConnectionCreatorService(stopRepository)

    override fun invoke(command: CreateConnectionCommand) =
        connectionCreatorService.invoke(
            sourceStopId = command.sourceStopId.toDomainId(),
            targetStopId = command.targetStopId.toDomainId(),
            distance = command.distance,
            allowedVehicleTypes = command.allowedVehicleTypes.map { VehicleType.valueOf(it) }.toSet()
        ).getOrHandle { throw it }
}

data class CreateConnectionCommand(
    val worldId: UUID,
    val sourceStopId: UUID,
    val targetStopId: UUID,
    val distance: Int,
    val allowedVehicleTypes: Set<String>
) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
