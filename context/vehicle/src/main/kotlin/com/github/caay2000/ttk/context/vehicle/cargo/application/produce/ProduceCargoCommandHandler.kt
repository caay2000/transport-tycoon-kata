package com.github.caay2000.ttk.context.vehicle.cargo.application.produce

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ProduceCargoCommandHandler(
    stopRepository: StopRepository
) : CommandHandler<ProduceCargoCommand> {

    private val produceCargoService = ProduceCargoService(stopRepository)

    override fun invoke(command: ProduceCargoCommand) {
        produceCargoService.invoke(
            stopId = command.stopId.toDomainId(),
            cargoId = command.cargoId.toDomainId(),
            targetId = command.targetStopId.toDomainId()
        ).getOrHandle { throw it }
    }
}

data class ProduceCargoCommand(val worldId: UUID, val stopId: UUID, val cargoId: UUID, val targetStopId: UUID) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
