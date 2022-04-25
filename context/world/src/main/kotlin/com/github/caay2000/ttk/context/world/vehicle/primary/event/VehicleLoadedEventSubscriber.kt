package com.github.caay2000.ttk.context.world.vehicle.primary.event

import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.world.cargo.application.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleLoadedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleLoadedEvent> {

    override fun handle(event: VehicleLoadedEvent) {
        commandBus.publish(ConsumeCargoCommand(event.worldId, event.stopId, event.cargoId))
    }
}
