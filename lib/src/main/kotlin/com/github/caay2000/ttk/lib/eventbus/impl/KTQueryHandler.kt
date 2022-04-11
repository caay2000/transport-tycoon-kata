package com.github.caay2000.ttk.lib.eventbus.impl

import kotlin.reflect.KClass

abstract class KTQueryHandler<in QUERY, out RESPONSE>(type: KClass<*>) {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<Any, QUERY, Any>().subscribe(this, type)
    }

    internal fun execute(query: Any): RESPONSE {
        @Suppress("UNCHECKED_CAST")
        return this.handle(query as QUERY)
    }

    abstract fun handle(query: QUERY): RESPONSE
}
