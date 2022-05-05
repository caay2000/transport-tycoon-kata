package com.github.caay2000.ttk.context.vehicle.cargo.primary.event

import com.github.caay2000.ttk.context.shared.event.VehicleLoadingEvent
import com.github.caay2000.ttk.context.vehicle.cargo.application.reserve.ReserveCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleLoadingEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleLoadingEvent> {

    override fun handle(event: VehicleLoadingEvent) = commandBus.publish(ReserveCargoCommand(event.worldId, event.stopId, event.cargoId))
}
