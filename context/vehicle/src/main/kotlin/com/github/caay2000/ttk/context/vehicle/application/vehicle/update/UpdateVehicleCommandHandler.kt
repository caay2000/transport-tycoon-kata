package com.github.caay2000.ttk.context.vehicle.application.vehicle.update

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus
import java.util.UUID

class UpdateVehicleCommandHandler(
    eventPublisher: EventPublisher<Event>,
    queryBus: QueryBus,
    vehicleRepository: VehicleRepository,
    stopRepository: StopRepository
) : CommandHandler<UpdateVehicleCommand> {

    private val vehicleUpdaterService = VehicleUpdaterService(eventPublisher, queryBus, vehicleRepository, stopRepository)

    override fun invoke(command: UpdateVehicleCommand) {
        vehicleUpdaterService.invoke(command.vehicleId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class UpdateVehicleCommand(val vehicleId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
