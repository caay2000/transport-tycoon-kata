package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.world.application.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.domain.Location

internal class Application(configuration: ApplicationConfiguration) {

    private val commandBus = configuration.commandBus
    private val eventRepository = configuration.eventRepository
    private val dateTimeProvider = configuration.dateTimeProvider

    fun execute(cargoDestinations: List<Location>): Result {

        val worldId = WorldId()

        commandBus.publish(CreateWorldCommand(worldId.id))
        cargoDestinations.forEach { destination ->
            commandBus.publish(AddCargoCommand(worldId.id, Location.FACTORY.name, destination.name))
        }

        commandBus.publish(UpdateWorldCommand(worldId.id))

        return Result(
            duration = dateTimeProvider.now().value(),
            events = eventRepository.getAll()
        )
    }
}

data class Result(
    val duration: Int,
    val events: List<Event>
)
