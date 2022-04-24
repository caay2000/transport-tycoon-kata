package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.shared.event.VehicleUnloadedEvent
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.unload.UnloadCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleUnloadedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleUnloadedEvent> {

    override fun handle(event: VehicleUnloadedEvent) = commandBus.publish(UnloadCargoCommand(event.worldId, event.stopId, event.cargoId, event.sourceStopId, event.targetStopId))
}
