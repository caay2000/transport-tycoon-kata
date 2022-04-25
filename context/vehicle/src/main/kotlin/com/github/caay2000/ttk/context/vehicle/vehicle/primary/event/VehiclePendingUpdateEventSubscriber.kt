package com.github.caay2000.ttk.context.vehicle.vehicle.primary.event

import com.github.caay2000.ttk.context.shared.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.context.vehicle.vehicle.application.update.UpdateVehicleCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehiclePendingUpdateEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehiclePendingUpdateEvent> {

    override fun handle(event: VehiclePendingUpdateEvent) = commandBus.publish(UpdateVehicleCommand(event.vehicleId))
}
