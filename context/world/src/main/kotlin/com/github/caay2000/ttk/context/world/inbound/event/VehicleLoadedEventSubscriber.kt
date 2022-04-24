package com.github.caay2000.ttk.context.world.inbound.event

import com.github.caay2000.ttk.context.shared.event.VehicleLoadedEvent
import com.github.caay2000.ttk.context.world.application.cargo.consume.ConsumeCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class VehicleLoadedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleLoadedEvent> {

    override fun handle(event: VehicleLoadedEvent) {
        commandBus.publish(ConsumeCargoCommand(event.worldId, event.stopId, event.cargoId))
    }
}
