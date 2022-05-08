package com.github.caay2000.ttk.context.vehicle.vehicle.application.update

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.domain.VehicleRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus
import java.util.UUID

class UpdateVehicleCommandHandler(
    eventPublisher: EventPublisher<Event>,
    queryBus: QueryBus,
    vehicleRepository: VehicleRepository
) : CommandHandler<UpdateVehicleCommand> {

    private val vehicleUpdaterService = VehicleUpdaterService(eventPublisher, queryBus, vehicleRepository)

    override fun invoke(command: UpdateVehicleCommand) {
        vehicleUpdaterService.invoke(command.vehicleId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class UpdateVehicleCommand(val vehicleId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
