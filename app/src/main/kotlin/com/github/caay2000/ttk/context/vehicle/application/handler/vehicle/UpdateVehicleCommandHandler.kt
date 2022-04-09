package com.github.caay2000.ttk.context.vehicle.application.handler.vehicle

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.vehicle.application.handler.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.repository.VehicleRepository
import com.github.caay2000.ttk.context.vehicle.application.service.VehicleUpdaterService
import java.util.UUID

class UpdateVehicleCommandHandler(eventPublisher: EventPublisher, vehicleRepository: VehicleRepository) : CommandHandler<UpdateVehicleCommand> {

    private val vehicleUpdaterService = VehicleUpdaterService(eventPublisher, vehicleRepository)

    override fun invoke(command: UpdateVehicleCommand) {
        vehicleUpdaterService.invoke(command.vehicleId.toDomainId())
            .getOrHandle { throw it }
    }
}

data class UpdateVehicleCommand(val vehicleId: UUID) : VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
