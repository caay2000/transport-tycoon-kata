package com.github.caay2000.ttk.context.core.domain

import com.github.caay2000.ttk.context.time.domain.DateTime

interface DateTimeProvider {

    fun now(): DateTime
    fun inc()
}
