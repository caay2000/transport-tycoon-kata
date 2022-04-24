package com.github.caay2000.ttk.context.world.application.cargo.consume

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.application.WorldCommand
import com.github.caay2000.ttk.context.world.domain.repository.StopRepository
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class ConsumeCargoCommandHandler(stopRepository: StopRepository) : CommandHandler<ConsumeCargoCommand> {

    private val consumeCargoService = ConsumeCargoService(stopRepository)

    override fun invoke(command: ConsumeCargoCommand) =
        consumeCargoService.invoke(stopId = command.stopId.toDomainId(), cargoId = command.cargoId.toDomainId())
            .getOrHandle { throw it }
}

data class ConsumeCargoCommand(override val worldId: UUID, val stopId: UUID, val cargoId: UUID) :
    WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
