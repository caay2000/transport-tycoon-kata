package com.github.caay2000.ttk.application.configuration

import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.shared.event.ConnectionCreatedEvent
import com.github.caay2000.ttk.context.shared.event.StopCreatedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.shared.event.VehicleLoadingEvent
import com.github.caay2000.ttk.context.shared.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.shared.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.vehicle.cargo.application.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.context.vehicle.cargo.application.consume.ConsumeCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.cargo.application.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.vehicle.cargo.application.produce.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.cargo.application.reserve.ReserveCargoCommand
import com.github.caay2000.ttk.context.vehicle.cargo.application.reserve.ReserveCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.cargo.application.unload.UnloadCargoCommand
import com.github.caay2000.ttk.context.vehicle.cargo.application.unload.UnloadCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.cargo.primary.event.CargoProducedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.cargo.primary.event.VehicleLoadedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.cargo.primary.event.VehicleLoadingEventSubscriber
import com.github.caay2000.ttk.context.vehicle.cargo.primary.event.VehicleUnloadedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQueryHandler
import com.github.caay2000.ttk.context.vehicle.stop.application.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.vehicle.stop.application.connection.create.CreateConnectionCommandHandler
import com.github.caay2000.ttk.context.vehicle.stop.application.create.CreateStopCommand
import com.github.caay2000.ttk.context.vehicle.stop.application.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.vehicle.stop.primary.event.ConnectionCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.stop.primary.event.StopCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.stop.secondary.database.InMemoryStopRepository
import com.github.caay2000.ttk.context.vehicle.vehicle.application.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.application.create.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.vehicle.application.update.UpdateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.application.update.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.vehicle.primary.event.VehicleCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.vehicle.primary.event.VehiclePendingUpdateEventSubscriber
import com.github.caay2000.ttk.context.vehicle.vehicle.secondary.database.InMemoryVehicleRepository
import com.github.caay2000.ttk.context.vehicle.world.application.create.CreateWorldCommand
import com.github.caay2000.ttk.context.vehicle.world.application.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.primary.event.WorldCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.secondary.database.InMemoryWorldRepository
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
        instantiateCommandHandler(ReserveCargoCommand::class, ReserveCargoCommandHandler(stopRepository))

        instantiateEventSubscriber(WorldCreatedEvent::class, WorldCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(StopCreatedEvent::class, StopCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(ConnectionCreatedEvent::class, ConnectionCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleCreatedEvent::class, VehicleCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehiclePendingUpdateEvent::class, VehiclePendingUpdateEventSubscriber(commandBus))
        instantiateEventSubscriber(CargoProducedEvent::class, CargoProducedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleLoadedEvent::class, VehicleLoadedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleUnloadedEvent::class, VehicleUnloadedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleLoadingEvent::class, VehicleLoadingEventSubscriber(commandBus))
    }
}
