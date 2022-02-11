package com.github.caay2000.ttk.lib.eventbus

import kotlin.reflect.KClass

abstract class KTEventSubscriber<in EVENT>(type: KClass<*>) {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<Any, EVENT>().subscribe(this, type)
    }

    internal fun execute(event: Any) {
        @Suppress("UNCHECKED_CAST")
        this.handle(event as EVENT)
    }

    abstract fun handle(event: EVENT)
}
