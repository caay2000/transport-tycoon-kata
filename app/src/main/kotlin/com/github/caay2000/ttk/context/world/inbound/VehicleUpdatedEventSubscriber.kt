package com.github.caay2000.ttk.context.world.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.world.application.handler.UpdateVehicleCommand

class VehicleUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleUpdatedEvent> {

    override fun handle(event: VehicleUpdatedEvent) {
        commandBus.publish(UpdateVehicleCommand(event.worldId, event.vehicleId, event.cargoId, event.status))
    }
}
