package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.world.application.handler.CreateVehicleCommand

class VehicleCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleCreatedEvent> {

    override fun handle(event: VehicleCreatedEvent) {
        commandBus.publish(CreateVehicleCommand(event.worldId, event.vehicleId, event.type, event.status))
    }
}
