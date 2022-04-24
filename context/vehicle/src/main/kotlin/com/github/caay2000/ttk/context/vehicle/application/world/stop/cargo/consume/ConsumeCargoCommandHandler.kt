package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.consume

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.domain.repository.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ConsumeCargoCommandHandler(
    stopRepository: StopRepository
) : CommandHandler<ConsumeCargoCommand> {

    private val consumeCargoService = ConsumeCargoService(stopRepository)

    override fun invoke(command: ConsumeCargoCommand) {
        consumeCargoService.invoke(command.stopId.toDomainId(), command.cargoId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class ConsumeCargoCommand(val worldId: UUID, val stopId: UUID, val cargoId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
