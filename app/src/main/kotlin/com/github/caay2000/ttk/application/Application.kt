package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.domain.randomDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.CreateVehicleCommand
import com.github.caay2000.ttk.context.vehicle.domain.VehicleType
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.domain.Location
import java.util.UUID

internal class Application(configuration: ApplicationConfiguration) {

    private val commandBus = configuration.commandBus
    private val eventRepository = configuration.eventRepository
    private val dateTimeProvider = configuration.dateTimeProvider

    fun execute(cargoDestinations: List<Location>): Resulte {

        val worldId = WorldId()

        commandBus.publish(CreateWorldCommand(worldId.uuid))

        cargoDestinations.forEach { destination ->
            commandBus.publish(AddCargoCommand(worldId.uuid, Location.FACTORY.name, destination.name))
        }

        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.TRUCK.name, "FACTORY"))
        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.TRUCK.name, "FACTORY"))
        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.BOAT.name, "PORT"))

        commandBus.publish(UpdateWorldCommand(worldId.uuid))

        return Resulte(
            duration = dateTimeProvider.now().value(),
            events = eventRepository.getAll()
        )
    }

    private fun randomVehicleId(): UUID = randomDomainId<VehicleId>().uuid
}
