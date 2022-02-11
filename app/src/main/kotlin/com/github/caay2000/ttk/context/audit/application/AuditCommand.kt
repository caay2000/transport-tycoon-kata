package com.github.caay2000.ttk.context.audit.application

import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.event.Event

interface AuditCommand : Command

data class EventPayload(val event: Event)
