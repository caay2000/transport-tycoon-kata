package com.github.caay2000.ttk.context.vehicle.inbound.event

import com.github.caay2000.ttk.context.shared.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.vehicle.application.vehicle.create.CreateVehicleCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleCreatedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleCreatedEvent> {

    override fun handle(event: VehicleCreatedEvent) = commandBus.publish(CreateVehicleCommand(event.worldId, event.stopId, event.vehicleId, event.type))
}
