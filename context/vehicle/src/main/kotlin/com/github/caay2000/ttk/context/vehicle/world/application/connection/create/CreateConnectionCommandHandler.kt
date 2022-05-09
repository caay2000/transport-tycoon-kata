package com.github.caay2000.ttk.context.vehicle.world.application.connection.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class CreateConnectionCommandHandler(worldRepository: WorldRepository) :
    CommandHandler<CreateConnectionCommand> {

    private val connectionCreatorService = ConnectionCreatorService(worldRepository)

    override fun invoke(command: CreateConnectionCommand) =
        connectionCreatorService.invoke(
            worldId = command.worldId.toDomainId(),
            sourceStopId = command.sourceStopId.toDomainId(),
            targetStopId = command.targetStopId.toDomainId(),
            distance = command.distance,
            allowedVehicleTypes = command.allowedVehicleTypes.map { VehicleTypeEnum.valueOf(it) }.toSet()
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
