package com.github.caay2000.ttk.context.vehicle.application.handler.world

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.application.service.CargoAdderService
import java.util.UUID

class AddCargoCommandHandler(worldRepository: WorldRepository) : CommandHandler<AddCargoCommand> {

    private val cargoAdderService = CargoAdderService(worldRepository)

    override fun invoke(command: AddCargoCommand) {
        cargoAdderService.invoke(command.worldId.toDomainId(), command.cargoId.toDomainId(), command.sourceId.toDomainId(), command.targetId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class AddCargoCommand(val worldId: UUID, val cargoId: UUID, val sourceId: UUID, val targetId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
