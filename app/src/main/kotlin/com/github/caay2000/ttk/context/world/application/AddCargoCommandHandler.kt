package com.github.caay2000.ttk.context.world.application

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.command.WorldCommand
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.context.world.domain.service.WorldConfiguratorService
import java.util.UUID

class AddCargoCommandHandler(private val worldConfigurator: WorldConfiguratorService) : CommandHandler<AddCargoCommand> {

    override fun invoke(command: AddCargoCommand) {
        val cargo = Cargo(origin = Location.valueOf(command.startPoint), destination = Location.valueOf(command.destination))
        worldConfigurator.addCargo(command.worldId.toDomainId(), cargo)
    }
}

data class AddCargoCommand(override val worldId: UUID, val startPoint: String, val destination: String) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
