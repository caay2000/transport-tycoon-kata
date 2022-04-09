package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.core.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.core.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.core.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.CreateVehicleCommand
import com.github.caay2000.ttk.context.world.application.handler.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.UpdateVehicleCommand
import com.github.caay2000.ttk.context.world.application.handler.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommandHandler
import com.github.caay2000.ttk.context.world.inbound.VehicleCreatedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.VehicleUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.WorldUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class WorldContextConfiguration(val commandBus: CommandBus<Command>, val eventPublisher: EventPublisher, database: InMemoryDatabase) {

    private val worldRepository = InMemoryWorldRepository(database)

    init {
        instantiateCommandHandler(AddCargoCommand::class, AddCargoCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(worldRepository))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, worldRepository))

        instantiateEventSubscriber(VehicleCreatedEvent::class, VehicleCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUpdatedEvent::class, VehicleUpdatedEventSubscriber(commandBus))
    }
}
