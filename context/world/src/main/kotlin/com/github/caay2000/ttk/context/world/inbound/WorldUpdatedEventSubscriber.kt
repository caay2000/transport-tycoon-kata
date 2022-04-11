package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand
import com.github.caay2000.ttk.lib.event.WorldUpdatedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class WorldUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<WorldUpdatedEvent> {

    override fun handle(event: WorldUpdatedEvent) {
        if (event.completed.not()) {
            commandBus.publish(UpdateWorldCommand(event.worldId))
        }
    }
}
