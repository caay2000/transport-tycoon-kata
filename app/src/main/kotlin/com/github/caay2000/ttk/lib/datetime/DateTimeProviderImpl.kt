package com.github.caay2000.ttk.lib.datetime

import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.time.domain.DateTime

class DateTimeProviderImpl : DateTimeProvider {

    private var dateTime: DateTime = DateTime(0)

    override fun now() = dateTime
    override fun inc() {
        dateTime = DateTime(dateTime.value().inc())
    }
}
