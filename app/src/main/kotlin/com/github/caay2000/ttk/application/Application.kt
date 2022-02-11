package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.world.application.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository

internal class Application(
    private val commandBus: CommandBus<Command>,
    private val eventRepository: EventRepository,
    private val worldRepository: WorldRepository,
    private val dateTimeProvider: DateTimeProvider
) {

    fun execute(cargoDestinations: List<Location>): Result {

        val worldId = WorldId()

        commandBus.publish(CreateWorldCommand(worldId.id))
        cargoDestinations.forEach { destination ->
            commandBus.publish(AddCargoCommand(worldId.id, Location.FACTORY.name, destination.name))
        }

        while (true) {
            val world = worldRepository.get(worldId)
            if (world.isCompleted().not()) {
                commandBus.publish(UpdateWorldCommand(worldId.id))
            } else break
        }

        val events = eventRepository.getAll()
        return Result(dateTimeProvider.now().value(), events)
    }
}

data class Result(
    val duration: Int,
    val events: List<Event>
)
