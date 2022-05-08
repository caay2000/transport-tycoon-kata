package com.github.caay2000.ttk.context.vehicle.world.primary.event

import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.vehicle.world.application.cargo.produce.ProduceCargoCommand
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class CargoProducedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<CargoProducedEvent> {

    override fun handle(event: CargoProducedEvent) = commandBus.publish(ProduceCargoCommand(event.worldId, event.sourceId, event.cargoId, event.targetId))
}
