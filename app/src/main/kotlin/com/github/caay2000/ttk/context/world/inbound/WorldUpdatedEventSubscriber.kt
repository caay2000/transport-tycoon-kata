package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand

class WorldUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<WorldUpdatedEvent> {

    override fun handle(event: WorldUpdatedEvent) {
        if (event.completed.not()) {
            commandBus.publish(UpdateWorldCommand(event.worldId))
        }
    }
}
