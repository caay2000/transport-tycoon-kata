package com.github.caay2000.ttk.lib.eventbus.impl

class KTCommandPublisher<in COMMAND> {

    fun publish(command: COMMAND) = KTEventBus.getInstance<COMMAND, Any>().publishCommand(command)
}
