package com.github.caay2000.ttk.context.audit.application

import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.event.Event

interface AuditCommand : Command

data class EventPayload(val event: Event)
