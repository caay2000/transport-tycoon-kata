package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.CargoAddedEvent
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.vehicle.application.handler.world.AddCargoCommand

class CargoAddedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<CargoAddedEvent> {

    override fun handle(event: CargoAddedEvent) = commandBus.publish(AddCargoCommand(event.worldId, event.cargoId, event.sourceId, event.targetId))
}
