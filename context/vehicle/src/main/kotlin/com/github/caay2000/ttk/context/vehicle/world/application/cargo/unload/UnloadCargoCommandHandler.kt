package com.github.caay2000.ttk.context.vehicle.world.application.cargo.unload

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class UnloadCargoCommandHandler(worldRepository: WorldRepository) : CommandHandler<UnloadCargoCommand> {

    private val unloadCargoService = UnloadCargoService(worldRepository)

    override fun invoke(command: UnloadCargoCommand) {
        unloadCargoService.invoke(
            worldId = command.worldId.toDomainId(),
            stopId = command.stopId.toDomainId(),
            cargoId = command.cargoId.toDomainId(),
            sourceStopId = command.sourceStopId.toDomainId(),
            targetStopId = command.targetStopId.toDomainId()
        ).getOrHandle { throw it }
    }
}

data class UnloadCargoCommand(val worldId: UUID, val stopId: UUID, val cargoId: UUID, val sourceStopId: UUID, val targetStopId: UUID) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
