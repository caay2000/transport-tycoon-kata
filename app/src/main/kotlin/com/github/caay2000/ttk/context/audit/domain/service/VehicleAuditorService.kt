package com.github.caay2000.ttk.context.audit.domain.service

import com.github.caay2000.ttk.context.audit.application.EventPayload
import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository

class VehicleAuditorService(private val eventRepository: EventRepository) {

    fun invoke(eventPayload: EventPayload) = eventRepository.save(eventPayload.event)
}
