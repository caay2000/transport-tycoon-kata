package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.vehicle.application.handler.world.CreateWorldCommand

class WorldCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<WorldCreatedEvent> {

    override fun handle(event: WorldCreatedEvent) = commandBus.publish(CreateWorldCommand(event.worldId))
}
