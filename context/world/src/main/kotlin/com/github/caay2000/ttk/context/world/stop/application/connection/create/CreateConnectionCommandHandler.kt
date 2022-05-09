package com.github.caay2000.ttk.context.world.stop.application.connection.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.WorldCommand
import com.github.caay2000.ttk.context.world.stop.domain.StopRepository
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class CreateConnectionCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository, stopRepository: StopRepository) :
    CommandHandler<CreateConnectionCommand> {

    private val connectionCreatorService = ConnectionCreatorService(eventPublisher, worldRepository, stopRepository)

    override fun invoke(command: CreateConnectionCommand) =
        connectionCreatorService.invoke(
            worldId = command.worldId.toDomainId(),
            sourceStopId = command.sourceStopId.toDomainId(),
            targetStopId = command.targetStopId.toDomainId(),
            distance = command.distance,
            allowedVehicleTypeEnums = command.allowedVehicleTypes.map { VehicleTypeEnum.valueOf(it) }.toSet()
        ).getOrHandle { throw it }
}

data class CreateConnectionCommand(override val worldId: UUID, val sourceStopId: UUID, val targetStopId: UUID, val distance: Int, val allowedVehicleTypes: Set<String>) :
    WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
