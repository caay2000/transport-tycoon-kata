package com.github.caay2000.ttk.context.vehicle.configuration.application.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.vehicle.VehicleCommand
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfigurationRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class CreateVehicleConfigurationCommandHandler(vehicleConfigurationRepository: VehicleConfigurationRepository) :
    CommandHandler<CreateVehicleConfigurationCommand> {

    private val vehicleConfigurationCreatorService = VehicleConfigurationCreatorService(vehicleConfigurationRepository)

    override fun invoke(command: CreateVehicleConfigurationCommand) {
        vehicleConfigurationCreatorService.invoke(command.type, command.loadTime, command.speed, command.capacity)
            .getOrHandle { throw it }
    }
}

data class CreateVehicleConfigurationCommand(val type: VehicleType, val loadTime: Int, val speed: Double, val capacity: Int) :
    VehicleCommand {
    override val commandId: UUID = UUID.randomUUID()
}
