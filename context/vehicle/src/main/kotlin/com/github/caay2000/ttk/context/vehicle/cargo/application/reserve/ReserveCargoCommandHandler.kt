package com.github.caay2000.ttk.context.vehicle.cargo.application.reserve

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.stop.domain.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ReserveCargoCommandHandler(stopRepository: StopRepository) : CommandHandler<ReserveCargoCommand> {

    private val produceCargoService = ReserveCargoService(stopRepository)

    override fun invoke(command: ReserveCargoCommand) {
        produceCargoService.invoke(
            stopId = command.stopId.toDomainId(),
            cargoId = command.cargoId.toDomainId()
        ).getOrHandle { throw it }
    }
}

data class ReserveCargoCommand(val worldId: UUID, val stopId: UUID, val cargoId: UUID) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
