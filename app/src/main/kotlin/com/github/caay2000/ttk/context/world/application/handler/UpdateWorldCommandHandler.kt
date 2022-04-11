package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.WorldUpdaterService
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class UpdateWorldCommandHandler(eventPublisher: EventPublisher<Event>, worldRepository: WorldRepository) : CommandHandler<UpdateWorldCommand> {

    private val worldUpdater = WorldUpdaterService(eventPublisher, worldRepository)

    override fun invoke(command: UpdateWorldCommand) {
        worldUpdater.invoke(command.worldId.toDomainId())
    }
}

data class UpdateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
