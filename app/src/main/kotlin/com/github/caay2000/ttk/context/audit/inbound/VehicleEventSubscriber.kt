package com.github.caay2000.ttk.context.audit.inbound

import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommand
import com.github.caay2000.ttk.context.audit.application.EventPayload
import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBus
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.vehicle.domain.VehicleEvent

class VehicleEventSubscriber(private val commandBus: CommandBus<Command>) : EventSubscriber<VehicleEvent> {

    override fun handle(event: VehicleEvent) = commandBus.publish(AuditVehicleCommand(EventPayload(event)))
}
