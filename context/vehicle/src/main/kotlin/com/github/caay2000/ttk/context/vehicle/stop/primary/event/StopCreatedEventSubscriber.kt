package com.github.caay2000.ttk.context.vehicle.stop.primary.event

import com.github.caay2000.ttk.context.shared.event.StopCreatedEvent
import com.github.caay2000.ttk.context.vehicle.stop.application.create.CreateStopCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class StopCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<StopCreatedEvent> {

    override fun handle(event: StopCreatedEvent) = commandBus.publish(CreateStopCommand(event.worldId, event.stopId, event.stopName))
}
