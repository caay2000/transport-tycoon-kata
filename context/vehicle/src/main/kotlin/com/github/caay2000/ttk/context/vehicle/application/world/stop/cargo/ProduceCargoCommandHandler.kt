package com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ProduceCargoCommandHandler(stopRepository: StopRepository) : CommandHandler<ProduceCargoCommand> {

    private val cargoProducerService = CargoProducerService(stopRepository)

    override fun invoke(command: ProduceCargoCommand) {
        cargoProducerService.invoke(command.cargoId.toDomainId(), command.sourceId.toDomainId(), command.targetId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class ProduceCargoCommand(val worldId: UUID, val cargoId: UUID, val sourceId: UUID, val targetId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
