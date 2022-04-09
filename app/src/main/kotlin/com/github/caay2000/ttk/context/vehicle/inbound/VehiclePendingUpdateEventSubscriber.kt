package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.context.vehicle.application.handler.vehicle.UpdateVehicleCommand

class VehiclePendingUpdateEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehiclePendingUpdateEvent> {

    override fun handle(event: VehiclePendingUpdateEvent) = commandBus.publish(UpdateVehicleCommand(event.vehicleId))
}
