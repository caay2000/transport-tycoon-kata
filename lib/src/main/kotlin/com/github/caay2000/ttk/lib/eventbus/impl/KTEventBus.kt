package com.github.caay2000.ttk.lib.eventbus.impl

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in COMMAND, in EVENT> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<*, *>
        fun <COMMAND, EVENT> init(): KTEventBus<COMMAND, EVENT> {
            eventBus = KTEventBus<COMMAND, EVENT>()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <COMMAND, EVENT> getInstance(): KTEventBus<COMMAND, EVENT> = eventBus as KTEventBus<COMMAND, EVENT>
    }

    private val commands: MutableList<COMMAND> = mutableListOf()
    private val commandSubscribers: MutableMap<KClass<*>, List<KTCommandHandler<*>>> = mutableMapOf()

    private val events: MutableList<EVENT> = mutableListOf()
    private val eventSubscribers: MutableMap<KClass<*>, List<KTEventSubscriber<*>>> = mutableMapOf()

    internal fun subscribe(subscriber: KTEventSubscriber<*>, type: KClass<*>) {
        eventSubscribers.getOrElse(type) { listOf() }.let {
            eventSubscribers[type] = it + subscriber
        }
    }

    internal fun subscribe(commandHandler: KTCommandHandler<*>, type: KClass<*>) {
        commandSubscribers.getOrElse(type) { listOf() }.let {
            commandSubscribers[type] = it + commandHandler
        }
    }

    internal fun publishEvent(event: EVENT) {
        events.add(event).also {
            notifyEventSubscribers(event)
        }
    }

    internal fun publishCommand(command: COMMAND) {
        commands.add(command).also {
            notifyCommandHandlers(command)
        }
    }

    private fun notifyEventSubscribers(event: EVENT) {

        eventSubscribers[event!!::class]?.forEach { subscriber ->
            subscriber.execute(event)
        }

        @Suppress("NULL_CHECK")
        event!!::class.superclasses.forEach { parent ->
            eventSubscribers[parent]?.forEach { subscriber ->
                subscriber.execute(event)
            }
        }
    }

    private fun notifyCommandHandlers(command: COMMAND) {
        commandSubscribers[command!!::class]?.forEach { commandHandler ->
            commandHandler.execute(command)
        }
    }

    internal fun getAllEvents(): List<@UnsafeVariance EVENT> = events.toList()

    @Suppress("UNCHECKED_CAST")
    internal fun <COMMAND> getAllCommands(): List<COMMAND> = commands.toList() as List<COMMAND>
}
