package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.world.application.handler.CreateVehicleCommand
import com.github.caay2000.ttk.lib.event.VehicleCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleCreatedEvent> {

    override fun handle(event: VehicleCreatedEvent) {
        commandBus.publish(CreateVehicleCommand(event.worldId, event.vehicleId, event.type, event.status))
    }
}
