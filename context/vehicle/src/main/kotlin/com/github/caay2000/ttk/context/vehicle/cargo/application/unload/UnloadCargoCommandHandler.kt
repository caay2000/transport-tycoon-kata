package com.github.caay2000.ttk.context.vehicle.cargo.application.unload

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class UnloadCargoCommandHandler(stopRepository: StopRepository) : CommandHandler<UnloadCargoCommand> {

    private val unloadCargoService = UnloadCargoService(stopRepository)

    override fun invoke(command: UnloadCargoCommand) {
        unloadCargoService.invoke(
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
