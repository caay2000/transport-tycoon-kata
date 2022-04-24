package com.github.caay2000.ttk.context.vehicle.inbound.event

import com.github.caay2000.ttk.context.shared.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.vehicle.application.world.create.CreateWorldCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class WorldCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<WorldCreatedEvent> {

    override fun handle(event: WorldCreatedEvent) = commandBus.publish(CreateWorldCommand(event.worldId))
}
