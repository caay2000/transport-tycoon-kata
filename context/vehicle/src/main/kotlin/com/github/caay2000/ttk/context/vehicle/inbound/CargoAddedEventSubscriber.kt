package com.github.caay2000.ttk.context.vehicle.inbound

import com.github.caay2000.ttk.context.vehicle.application.handler.world.AddCargoCommand
import com.github.caay2000.ttk.lib.event.CargoAddedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBus
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber

class CargoAddedEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<CargoAddedEvent> {

    override fun handle(event: CargoAddedEvent) = commandBus.publish(AddCargoCommand(event.worldId, event.cargoId, event.sourceId, event.targetId))
}
