package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.vehicle.application.handler.world.CreateStopCommand
import com.github.caay2000.ttk.lib.event.StopCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class StopCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<StopCreatedEvent> {

    override fun handle(event: StopCreatedEvent) = commandBus.publish(CreateStopCommand(event.worldId, event.stopId, event.stopName))
}
