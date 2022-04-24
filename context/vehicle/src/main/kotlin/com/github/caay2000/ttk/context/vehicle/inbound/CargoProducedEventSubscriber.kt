package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.vehicle.application.world.stop.cargo.produce.ProduceCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class CargoProducedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<CargoProducedEvent> {

    override fun handle(event: CargoProducedEvent) = commandBus.publish(ProduceCargoCommand(event.worldId, event.sourceId, event.cargoId, event.targetId))
}
