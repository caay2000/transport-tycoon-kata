package com.github.caay2000.ttk.context.world.application.cargo.produce

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.WorldCommand
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisher
import java.util.UUID

class ProduceCargoCommandHandler(eventPublisher: EventPublisher<Event>, stopRepository: StopRepository) : CommandHandler<ProduceCargoCommand> {

    private val produceCargoService = ProduceCargoService(eventPublisher, stopRepository)

    override fun invoke(command: ProduceCargoCommand) {
        produceCargoService.invoke(
            stopId = command.stopId.toDomainId(),
            cargoId = command.cargoId.toDomainId(),
            targetId = command.targetStopId.toDomainId()
        ).getOrHandle { throw it }
    }
}

data class ProduceCargoCommand(override val worldId: UUID, val stopId: UUID, val cargoId: UUID, val targetStopId: UUID) :
    WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
