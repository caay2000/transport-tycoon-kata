package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.StopCreatedEvent
import com.github.caay2000.ttk.context.vehicle.application.handler.world.CreateStopCommand

class StopCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<StopCreatedEvent> {

    override fun handle(event: StopCreatedEvent) = commandBus.publish(CreateStopCommand(event.worldId, event.stopId, event.stopName))

}
