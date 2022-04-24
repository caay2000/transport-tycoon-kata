package com.github.caay2000.ttk.application.configuration

import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.shared.event.ConnectionCreatedEvent
import com.github.caay2000.ttk.context.shared.event.StopCreatedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQueryHandler
import com.github.caay2000.ttk.context.vehicle.application.vehicle.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.vehicle.create.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.vehicle.update.UpdateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.vehicle.update.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.create.CreateWorldCommand
import com.github.caay2000.ttk.context.vehicle.application.world.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.consume.ConsumeCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.produce.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.unload.UnloadCargoCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.unload.UnloadCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.connection.create.CreateConnectionCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.create.CreateStopCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.vehicle.inbound.CargoProducedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.VehicleLoadedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.VehiclePendingUpdateEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.VehicleUnloadedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.event.ConnectionCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.event.StopCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.event.VehicleCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.event.WorldCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryStopRepository
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryVehicleRepository
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import com.github.caay2000.ttk.lib.eventbus.query.QueryBus

class VehicleContextConfiguration(
    commandBus: CommandBus<Command>,
    queryBus: QueryBus,
    eventPublisher: EventPublisher<Event>,
    database: InMemoryDatabase
) {

    private val worldRepository = InMemoryWorldRepository(database)
    private val stopRepository = InMemoryStopRepository(database)
    private val vehicleRepository = InMemoryVehicleRepository(database)

    init {
        instantiateQueryHandler(FindRouteQuery::class, FindRouteQueryHandler(vehicleRepository, stopRepository))

        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(vehicleRepository, stopRepository))
        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, queryBus, vehicleRepository, stopRepository))
        instantiateCommandHandler(CreateStopCommand::class, CreateStopCommandHandler(worldRepository))
        instantiateCommandHandler(CreateConnectionCommand::class, CreateConnectionCommandHandler(stopRepository))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(worldRepository))
        instantiateCommandHandler(ProduceCargoCommand::class, ProduceCargoCommandHandler(stopRepository))
        instantiateCommandHandler(ConsumeCargoCommand::class, ConsumeCargoCommandHandler(stopRepository))
        instantiateCommandHandler(UnloadCargoCommand::class, UnloadCargoCommandHandler(stopRepository))

        instantiateEventSubscriber(WorldCreatedEvent::class, WorldCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(StopCreatedEvent::class, StopCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(ConnectionCreatedEvent::class, ConnectionCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleCreatedEvent::class, VehicleCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehiclePendingUpdateEvent::class, VehiclePendingUpdateEventSubscriber(commandBus))
        instantiateEventSubscriber(CargoProducedEvent::class, CargoProducedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleLoadedEvent::class, VehicleLoadedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUnloadedEvent::class, VehicleUnloadedEventSubscriber(commandBus))
    }
}
