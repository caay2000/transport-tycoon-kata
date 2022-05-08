package com.github.caay2000.ttk.context.vehicle.cargo.application.consume

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ConsumeCargoCommandHandler(
    worldRepository: WorldRepository
) : CommandHandler<ConsumeCargoCommand> {

    private val consumeCargoService = ConsumeCargoService(worldRepository)

    override fun invoke(command: ConsumeCargoCommand) {
        consumeCargoService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.cargoId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class ConsumeCargoCommand(val worldId: UUID, val stopId: UUID, val cargoId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
