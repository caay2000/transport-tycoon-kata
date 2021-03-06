package com.github.caay2000.ttk.context.world.vehicle.application.update

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.WorldCommand
import com.github.caay2000.ttk.context.world.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class UpdateVehicleCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository) :
    CommandHandler<UpdateVehicleCommand> {

    private val vehicleUpdaterService = VehicleUpdaterService(eventPublisher, worldRepository)

    override fun invoke(command: UpdateVehicleCommand) =
        vehicleUpdaterService.invoke(command.worldId.toDomainId(), command.vehicleId.toDomainId(), command.cargoId?.toDomainId(), command.dateTimeHash)
            .getOrHandle { throw it }
}

data class UpdateVehicleCommand(
    override val worldId: UUID,
    val vehicleId: UUID,
    val cargoId: UUID?,
    val status: String,
    val dateTimeHash: String
) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
