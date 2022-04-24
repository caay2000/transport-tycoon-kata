package com.github.caay2000.ttk.application.configuration

import com.github.caay2000.ttk.context.world.inbound.rest.HttpController
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProvider
import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBusImpl
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisherImpl
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber
import com.github.caay2000.ttk.lib.eventbus.impl.KTCommandHandler
import com.github.caay2000.ttk.lib.eventbus.impl.KTEventBus
import com.github.caay2000.ttk.lib.eventbus.impl.KTEventSubscriber
import com.github.caay2000.ttk.lib.eventbus.impl.KTQueryHandler
import com.github.caay2000.ttk.lib.eventbus.query.Query
import com.github.caay2000.ttk.lib.eventbus.query.QueryBusImpl
import com.github.caay2000.ttk.lib.eventbus.query.QueryHandler
import kotlin.reflect.KClass

class ApplicationConfiguration {

    val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

    private val commandBus = CommandBusImpl()
    private val queryBus = QueryBusImpl()
    private val eventPublisher = EventPublisherImpl()

    val httpController: HttpController = HttpController(commandBus, queryBus)

    private val database: InMemoryDatabase = InMemoryDatabase()

    init {
        KTEventBus.init<Command, Query, Event>()
        WorldContextConfiguration(commandBus, eventPublisher, database, dateTimeProvider)
        VehicleContextConfiguration(commandBus, queryBus, eventPublisher, database)
    }
}

internal fun <COMMAND : Command> instantiateCommandHandler(clazz: KClass<COMMAND>, commandHandler: CommandHandler<COMMAND>): KTCommandHandler<COMMAND> =
    object : KTCommandHandler<COMMAND>(clazz) {
        override fun invoke(command: COMMAND) = commandHandler.invoke(command)
    }

internal fun <EVENT : Event> instantiateEventSubscriber(clazz: KClass<EVENT>, eventSubscriber: EventSubscriber<EVENT>): KTEventSubscriber<EVENT> =
    object : KTEventSubscriber<EVENT>(clazz) {
        override fun handle(event: EVENT) = eventSubscriber.handle(event)
    }

internal fun <QUERY : Query, RESPONSE : Any> instantiateQueryHandler(clazz: KClass<QUERY>, queryHandler: QueryHandler<QUERY, RESPONSE>): KTQueryHandler<QUERY, RESPONSE> =
    object : KTQueryHandler<QUERY, RESPONSE>(clazz) {
        override fun handle(query: QUERY): RESPONSE = queryHandler.handle(query)
    }
