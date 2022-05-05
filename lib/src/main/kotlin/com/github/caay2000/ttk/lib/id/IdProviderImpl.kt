package com.github.caay2000.ttk.lib.id

import java.util.UUID

class IdProviderImpl : IdProvider {

    override fun generateId(): UUID = UUID.randomUUID()
}
