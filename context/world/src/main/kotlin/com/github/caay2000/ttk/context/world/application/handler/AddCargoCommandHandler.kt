package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.shared.domain.Location
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.WorldConfiguratorService
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class AddCargoCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository) :
    CommandHandler<AddCargoCommand> {

    private val worldConfigurator = WorldConfiguratorService(eventPublisher, worldRepository)

    override fun invoke(command: AddCargoCommand) {
        val cargo = Cargo(origin = Location.valueOf(command.startPoint), destination = Location.valueOf(command.destination))
        worldConfigurator.addCargo(command.worldId.toDomainId(), cargo)
    }
}

data class AddCargoCommand(override val worldId: UUID, val startPoint: String, val destination: String) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
