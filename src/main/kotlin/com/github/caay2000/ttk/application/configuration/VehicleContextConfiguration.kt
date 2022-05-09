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
import com.github.caay2000.ttk.context.vehicle.configuration.application.create.CreateVehicleConfigurationCommand
import com.github.caay2000.ttk.context.vehicle.configuration.application.create.CreateVehicleConfigurationCommandHandler
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQuery
import com.github.caay2000.ttk.context.vehicle.configuration.application.find.FindVehicleConfigurationQueryHandler
import com.github.caay2000.ttk.context.vehicle.configuration.secondary.database.InMemoryVehicleConfigurationRepository
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.route.application.find.FindRouteQueryHandler
import com.github.caay2000.ttk.context.vehicle.vehicle.application.create.CreateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.application.create.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.vehicle.application.update.UpdateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.vehicle.application.update.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.vehicle.primary.event.VehicleCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.vehicle.primary.event.VehiclePendingUpdateEventSubscriber
import com.github.caay2000.ttk.context.vehicle.vehicle.secondary.database.InMemoryVehicleRepository
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.consume.ConsumeCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.produce.ProduceCargoCommand
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.produce.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.reserve.ReserveCargoCommand
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.reserve.ReserveCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.unload.UnloadCargoCommand
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.unload.UnloadCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.context.vehicle.world.application.connection.create.CreateConnectionCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.create.CreateWorldCommand
import com.github.caay2000.ttk.context.vehicle.world.application.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.application.stop.create.CreateStopCommand
import com.github.caay2000.ttk.context.vehicle.world.application.stop.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.vehicle.world.primary.event.CargoProducedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.primary.event.ConnectionCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.primary.event.StopCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.primary.event.VehicleLoadedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.primary.event.VehicleLoadingEventSubscriber
import com.github.caay2000.ttk.context.vehicle.world.primary.event.VehicleUnloadedEventSubscriber
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
    private val vehicleRepository = InMemoryVehicleRepository(database)
    private val vehicleConfigurationRepository = InMemoryVehicleConfigurationRepository(database)

    init {
        instantiateQueryHandler(FindRouteQuery::class, FindRouteQueryHandler(vehicleRepository, worldRepository))
        instantiateQueryHandler(FindVehicleConfigurationQuery::class, FindVehicleConfigurationQueryHandler(vehicleConfigurationRepository))

        instantiateCommandHandler(CreateVehicleConfigurationCommand::class, CreateVehicleConfigurationCommandHandler(vehicleConfigurationRepository))
        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(queryBus, vehicleRepository))
        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, queryBus, vehicleRepository))
        instantiateCommandHandler(CreateStopCommand::class, CreateStopCommandHandler(worldRepository))
        instantiateCommandHandler(CreateConnectionCommand::class, CreateConnectionCommandHandler(worldRepository))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(worldRepository))
        instantiateCommandHandler(ProduceCargoCommand::class, ProduceCargoCommandHandler(worldRepository))
        instantiateCommandHandler(ConsumeCargoCommand::class, ConsumeCargoCommandHandler(worldRepository))
        instantiateCommandHandler(UnloadCargoCommand::class, UnloadCargoCommandHandler(worldRepository))
        instantiateCommandHandler(ReserveCargoCommand::class, ReserveCargoCommandHandler(worldRepository))

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
