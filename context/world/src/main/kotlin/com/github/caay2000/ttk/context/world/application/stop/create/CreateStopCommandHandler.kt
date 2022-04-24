package com.github.caay2000.ttk.context.world.application.stop.create

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.WorldCommand
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class CreateStopCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository, stopRepository: StopRepository) : CommandHandler<CreateStopCommand> {

    private val stopCreatorService = StopCreatorService(eventPublisher, worldRepository, stopRepository)

    override fun invoke(command: CreateStopCommand) =
        stopCreatorService.invoke(command.worldId.toDomainId(), command.stopId.toDomainId(), command.stopName)
            .getOrHandle { throw it }
}

data class CreateStopCommand(override val worldId: UUID, val stopId: UUID, val stopName: String) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
