package com.github.caay2000.ttk.context.vehicle.stop.primary.event

import com.github.caay2000.ttk.context.shared.event.ConnectionCreatedEvent
import com.github.caay2000.ttk.context.vehicle.stop.application.connection.create.CreateConnectionCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class ConnectionCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<ConnectionCreatedEvent> {

    override fun handle(event: ConnectionCreatedEvent) =
        commandBus.publish(CreateConnectionCommand(event.worldId, event.sourceStopId, event.targetStopId, event.distance, event.allowedVehicleTypes))
}
