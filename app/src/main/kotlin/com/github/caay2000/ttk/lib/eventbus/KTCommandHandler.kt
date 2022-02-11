package com.github.caay2000.ttk.lib.eventbus

import kotlin.reflect.KClass

abstract class KTCommandHandler<in COMMAND>(type: KClass<*>) {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<*>) {
        KTEventBus.getInstance<COMMAND, Any>().subscribe(this, type)
    }

    internal fun execute(command: Any) {
        @Suppress("UNCHECKED_CAST")
        this.invoke(command as COMMAND)
    }

    abstract fun invoke(command: COMMAND)
}
