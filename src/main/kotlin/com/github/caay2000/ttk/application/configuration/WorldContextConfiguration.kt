package com.github.caay2000.ttk.application.configuration

import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.shared.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.application.cargo.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.context.world.application.cargo.consume.ConsumeCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.cargo.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.world.application.cargo.produce.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.cargo.unload.UnloadCargoCommand
import com.github.caay2000.ttk.context.world.application.cargo.unload.UnloadCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.stop.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.world.application.stop.connection.create.CreateConnectionCommandHandler
import com.github.caay2000.ttk.context.world.application.stop.create.CreateStopCommand
import com.github.caay2000.ttk.context.world.application.stop.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.world.application.vehicle.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.world.application.vehicle.create.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.application.vehicle.update.UpdateVehicleCommand
import com.github.caay2000.ttk.context.world.application.vehicle.update.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.application.world.create.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.world.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.world.application.world.find.FindWorldQuery
import com.github.caay2000.ttk.context.world.application.world.find.FindWorldQueryHandler
import com.github.caay2000.ttk.context.world.application.world.update.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.application.world.update.UpdateWorldCommandHandler
import com.github.caay2000.ttk.context.world.inbound.event.VehicleLoadedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.event.VehicleUnloadedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.event.VehicleUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.event.WorldUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.outbound.InMemoryStopRepository
import com.github.caay2000.ttk.context.world.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldContextConfiguration(commandBus: CommandBus<Command>, eventPublisher: EventPublisher<Event>, database: InMemoryDatabase) {

    private val worldRepository = InMemoryWorldRepository(database)
    private val stopRepository = InMemoryStopRepository(database)

    init {
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(CreateStopCommand::class, CreateStopCommandHandler(eventPublisher, worldRepository, stopRepository))
        instantiateCommandHandler(CreateConnectionCommand::class, CreateConnectionCommandHandler(eventPublisher, worldRepository, stopRepository))
        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(ProduceCargoCommand::class, ProduceCargoCommandHandler(eventPublisher, stopRepository))
        instantiateCommandHandler(ConsumeCargoCommand::class, ConsumeCargoCommandHandler(stopRepository))
        instantiateCommandHandler(UnloadCargoCommand::class, UnloadCargoCommandHandler(stopRepository))
        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, worldRepository))

//        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(worldRepository))
//        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(eventPublisher, worldRepository))
//        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, worldRepository))

        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUpdatedEvent::class, VehicleUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleLoadedEvent::class, VehicleLoadedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUnloadedEvent::class, VehicleUnloadedEventSubscriber(commandBus))

        instantiateQueryHandler(FindWorldQuery::class, FindWorldQueryHandler(worldRepository))
    }
}
