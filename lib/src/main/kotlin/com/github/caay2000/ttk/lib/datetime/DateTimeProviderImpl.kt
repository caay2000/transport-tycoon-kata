package com.github.caay2000.ttk.lib.datetime

import java.util.UUID

class DateTimeProviderImpl : DateTimeProvider {

    private var dateTime: DateTime = DateTime(0)
    private var hash: UUID = UUID.randomUUID()

    override fun now() = dateTime
    override fun inc() {
        dateTime = DateTime(dateTime.value().inc())
        hash = UUID.randomUUID()
    }

    override fun hash(): String = hash.toString()
}
