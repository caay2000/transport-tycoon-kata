package com.github.caay2000.ttk.context.world.inbound.event

import com.github.caay2000.ttk.context.shared.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.application.time.UpdateDateTimeCommand
import com.github.caay2000.ttk.context.world.application.world.update.UpdateWorldCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class WorldUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<WorldUpdatedEvent> {

    override fun handle(event: WorldUpdatedEvent) {
        if (event.completed.not()) {
            commandBus.publish(UpdateDateTimeCommand(event.worldId))
            commandBus.publish(UpdateWorldCommand(event.worldId))
        }
    }
}
