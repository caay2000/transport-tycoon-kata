package com.github.caay2000.ttk.context.audit.domain.service

import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.vehicle.domain.VehicleEvent

class VehicleAuditorService(private val eventRepository: EventRepository) {

    fun invoke(event: VehicleEvent) = eventRepository.save(event)
}
