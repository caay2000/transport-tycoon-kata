package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.CreateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.CreateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.UpdateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.UpdateVehicleCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQuery
import com.github.caay2000.ttk.context.vehicle.application.route.FindRouteQueryHandler
import com.github.caay2000.ttk.context.vehicle.application.world.create.CreateWorldCommand
import com.github.caay2000.ttk.context.vehicle.application.world.create.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.ProduceCargoCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.ProduceCargoCommandHandler
import com.github.caay2000.ttk.context.vehicle.application.world.stop.create.CreateStopCommand
import com.github.caay2000.ttk.context.vehicle.application.world.stop.create.CreateStopCommandHandler
import com.github.caay2000.ttk.context.vehicle.inbound.CargoAddedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.StopCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.VehiclePendingUpdateEventSubscriber
import com.github.caay2000.ttk.context.vehicle.inbound.WorldCreatedEventSubscriber
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryStopRepository
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryVehicleRepository
import com.github.caay2000.ttk.context.vehicle.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.event.CargoAddedEvent
import com.github.caay2000.ttk.lib.event.StopCreatedEvent
import com.github.caay2000.ttk.lib.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.lib.event.WorldCreatedEvent
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

        instantiateCommandHandler(CreateVehicleCommand::class, CreateVehicleCommandHandler(eventPublisher, queryBus, worldRepository, vehicleRepository))
        instantiateCommandHandler(UpdateVehicleCommand::class, UpdateVehicleCommandHandler(eventPublisher, vehicleRepository))
        instantiateCommandHandler(CreateStopCommand::class, CreateStopCommandHandler(worldRepository))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(worldRepository))
        instantiateCommandHandler(ProduceCargoCommand::class, ProduceCargoCommandHandler(stopRepository))

        instantiateEventSubscriber(StopCreatedEvent::class, StopCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehiclePendingUpdateEvent::class, VehiclePendingUpdateEventSubscriber(commandBus))
        instantiateEventSubscriber(WorldCreatedEvent::class, WorldCreatedEventSubscriber(commandBus))
        instantiateEventSubscriber(CargoAddedEvent::class, CargoAddedEventSubscriber(commandBus))
    }
}
