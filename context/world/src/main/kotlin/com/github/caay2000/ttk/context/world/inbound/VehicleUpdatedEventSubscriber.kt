package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.world.application.handler.UpdateVehicleCommand
import com.github.caay2000.ttk.lib.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleUpdatedEvent> {

    override fun handle(event: VehicleUpdatedEvent) {
        commandBus.publish(UpdateVehicleCommand(event.worldId, event.vehicleId, event.cargoId, event.status))
    }
}
