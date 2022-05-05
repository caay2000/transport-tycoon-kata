package com.github.caay2000.ttk.context.world.vehicle.primary.event

import com.github.caay2000.ttk.context.shared.event.VehicleUpdatedEvent
import com.github.caay2000.ttk.context.world.vehicle.application.update.UpdateVehicleCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleUpdatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleUpdatedEvent> {

    override fun handle(event: VehicleUpdatedEvent) {
        commandBus.publish(UpdateVehicleCommand(event.worldId, event.vehicleId, event.cargoId, event.status))
    }
}
