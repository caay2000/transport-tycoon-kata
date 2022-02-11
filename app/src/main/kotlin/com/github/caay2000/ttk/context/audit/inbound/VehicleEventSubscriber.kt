package com.github.caay2000.ttk.context.audit.inbound

import com.github.caay2000.ttk.context.audit.domain.service.VehicleAuditorService
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.vehicle.domain.VehicleEvent

class VehicleEventSubscriber(private val vehicleAuditor: VehicleAuditorService) : EventSubscriber<VehicleEvent> {

    override fun handle(event: VehicleEvent) = vehicleAuditor.invoke(event)
}
