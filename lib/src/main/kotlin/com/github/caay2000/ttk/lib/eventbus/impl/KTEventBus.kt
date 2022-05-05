package com.github.caay2000.ttk.lib.eventbus.impl

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class KTEventBus<in COMMAND, in QUERY, in EVENT> private constructor() {

    companion object {

        private lateinit var eventBus: KTEventBus<*, *, *>
        fun <COMMAND, QUERY, EVENT> init(): KTEventBus<COMMAND, QUERY, EVENT> {
            eventBus = KTEventBus<COMMAND, QUERY, EVENT>()
            return getInstance()
        }

        @Suppress("UNCHECKED_CAST")
        fun <COMMAND, QUERY, EVENT> getInstance(): KTEventBus<COMMAND, QUERY, EVENT> = eventBus as KTEventBus<COMMAND, QUERY, EVENT>
    }

    private val commands: MutableList<COMMAND> = mutableListOf()
    private val commandSubscribers: MutableMap<KClass<*>, List<KTCommandHandler<*>>> = mutableMapOf()

    private val queries: MutableList<QUERY> = mutableListOf()
    private val queryHandlers: MutableMap<KClass<*>, KTQueryHandler<*, *>> = mutableMapOf()

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

    internal fun subscribe(queryHandler: KTQueryHandler<*, *>, type: KClass<*>) {
        queryHandlers.getOrElse(type) { null }.let {
            queryHandlers[type] = queryHandler
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

    internal fun <RESPONSE> publishQuery(query: QUERY): RESPONSE =
        queries.add(query)
            .let {
                @Suppress("UNCHECKED_CAST")
                queryHandlers[query!!::class]!!.execute(query) as RESPONSE
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
