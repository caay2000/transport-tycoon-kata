package com.github.caay2000.ttk.application.configuration

import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.shared.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.cargo.application.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.context.world.cargo.application.consume.ConsumeCargoCommandHandler
import com.github.caay2000.ttk.context.world.cargo.application.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.world.cargo.application.produce.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.world.cargo.application.unload.UnloadCargoCommand
import com.github.caay2000.ttk.context.world.cargo.application.unload.UnloadCargoCommandHandler
import com.github.caay2000.ttk.context.world.stop.application.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.world.stop.application.connection.create.CreateConnectionCommandHandler
import com.github.caay2000.ttk.context.world.stop.application.create.CreateStopCommand
import com.github.caay2000.ttk.context.world.stop.application.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.world.stop.secondary.database.InMemoryStopRepository
import com.github.caay2000.ttk.context.world.time.application.UpdateDateTimeCommand
import com.github.caay2000.ttk.context.world.time.application.UpdateDateTimeCommandHandler
import com.github.caay2000.ttk.context.world.vehicle.application.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.world.vehicle.application.create.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.vehicle.application.update.UpdateVehicleCommand
import com.github.caay2000.ttk.context.world.vehicle.application.update.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.world.vehicle.primary.event.VehicleLoadedEventSubscriber
import com.github.caay2000.ttk.context.world.vehicle.primary.event.VehicleUnloadedEventSubscriber
import com.github.caay2000.ttk.context.world.vehicle.primary.event.VehicleUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.world.application.create.CreateWorldCommand
import com.github.caay2000.ttk.context.world.world.application.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.world.world.application.find.FindWorldQuery
import com.github.caay2000.ttk.context.world.world.application.find.FindWorldQueryHandler
import com.github.caay2000.ttk.context.world.world.application.update.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.world.application.update.UpdateWorldCommandHandler
import com.github.caay2000.ttk.context.world.world.primary.event.WorldUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.world.secondary.database.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProvider
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher

class WorldContextConfiguration(commandBus: CommandBus<Command>, eventPublisher: EventPublisher<Event>, database: InMemoryDatabase, dateTimeProvider: DateTimeProvider) {

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
        instantiateCommandHandler(UpdateDateTimeCommand::class, UpdateDateTimeCommandHandler(dateTimeProvider))

//        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(worldRepository))
//        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(eventPublisher, worldRepository))
//        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, worldRepository))

        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUpdatedEvent::class, VehicleUpdatedEventSubscriber(commandBus, dateTimeProvider))
        instantiateEventSubscriber(VehicleLoadedEvent::class, VehicleLoadedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUnloadedEvent::class, VehicleUnloadedEventSubscriber(commandBus))

        instantiateQueryHandler(FindWorldQuery::class, FindWorldQueryHandler(worldRepository))
    }
}
