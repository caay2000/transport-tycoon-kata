package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.shared.domain.Location
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.domain.randomDomainId
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.CreateVehicleCommand
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand
import com.github.caay2000.ttk.lib.eventbus.event.Event
import java.util.UUID

class Application(configuration: ApplicationConfiguration) {

    private val commandBus = configuration.commandBus
    private val dateTimeProvider = configuration.dateTimeProvider

    fun execute(cargoDestinations: List<Location>): Result {

        val worldId = WorldId()

        commandBus.publish(CreateWorldCommand(worldId.uuid))

        cargoDestinations.forEach { destination ->
            commandBus.publish(AddCargoCommand(worldId.uuid, Location.FACTORY.name, destination.name))
        }

        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.TRUCK.name, "FACTORY"))
        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.TRUCK.name, "FACTORY"))
        commandBus.publish(CreateVehicleCommand(worldId.uuid, randomVehicleId(), VehicleType.BOAT.name, "PORT"))

        commandBus.publish(UpdateWorldCommand(worldId.uuid))

        return Result(
            duration = dateTimeProvider.now().value(),
            events = emptyList()
        )
    }

    private fun randomVehicleId(): UUID = randomDomainId<VehicleId>().uuid

    class Result(
        val duration: Int,
        val events: List<Event>
    )
}
