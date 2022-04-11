package com.github.caay2000.ttk.context.audit.application

import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.audit.domain.service.VehicleAuditorService
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import java.util.UUID

class AuditVehicleCommandHandler(eventRepository: EventRepository) : CommandHandler<AuditVehicleCommand> {

    private val vehicleAuditor = VehicleAuditorService(eventRepository)

    override fun invoke(command: AuditVehicleCommand) {
        vehicleAuditor.invoke(command.event)
    }
}

data class AuditVehicleCommand(val event: EventPayload) : AuditCommand {
    override val commandId: UUID = UUID.randomUUID()
}
