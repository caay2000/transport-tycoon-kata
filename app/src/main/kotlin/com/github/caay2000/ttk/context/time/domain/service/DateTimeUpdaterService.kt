package com.github.caay2000.ttk.context.time.domain.service

import com.github.caay2000.ttk.context.core.domain.DateTimeProvider

class DateTimeUpdaterService(private val dateTimeProvider: DateTimeProvider) {

    fun invoke() = dateTimeProvider.inc()
}
