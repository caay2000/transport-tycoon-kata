package com.github.caay2000.ttk.context.world.cargo.application.consume

import arrow.core.getOrHandle
import com.github.caay2000.ttk.context.shared.domain.toDomainId
import com.github.caay2000.ttk.context.world.WorldCommand
import com.github.caay2000.ttk.context.world.stop.domain.StopRepository
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
