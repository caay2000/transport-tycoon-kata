package com.github.caay2000.ttk.lib.eventbus.query

import java.util.UUID

interface Query {

    val queryId: UUID
    fun type(): String = this::class.java.simpleName

    fun randomUUID(): UUID = UUID.randomUUID()
}
