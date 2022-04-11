package com.github.caay2000.ttk.lib.id

import java.util.UUID

interface IdProvider {

    fun generateId(): UUID
}
