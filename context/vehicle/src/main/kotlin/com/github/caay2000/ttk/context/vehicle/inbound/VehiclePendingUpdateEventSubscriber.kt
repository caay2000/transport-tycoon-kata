package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.vehicle.application.vehicle.update.UpdateVehicleCommand
import com.github.caay2000.ttk.lib.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehiclePendingUpdateEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehiclePendingUpdateEvent> {

    override fun handle(event: VehiclePendingUpdateEvent) = commandBus.publish(UpdateVehicleCommand(event.vehicleId))
}
