package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommand
import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommandHandler
import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.audit.outbound.InMemoryEventRepository
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommand
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommandHandler
import com.github.caay2000.ttk.context.time.inbound.WorldUpdatedEventSubscriber
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProvider
import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
import com.github.caay2000.ttk.lib.event.WorldUpdatedEvent
import com.github.caay2000.ttk.lib.eventbus.command.Command
import com.github.caay2000.ttk.lib.eventbus.command.CommandBusImpl
import com.github.caay2000.ttk.lib.eventbus.command.CommandHandler
import com.github.caay2000.ttk.lib.eventbus.event.Event
import com.github.caay2000.ttk.lib.eventbus.event.EventPublisherImpl
import com.github.caay2000.ttk.lib.eventbus.event.EventSubscriber
import com.github.caay2000.ttk.lib.eventbus.impl.KTCommandHandler
import com.github.caay2000.ttk.lib.eventbus.impl.KTEventBus
import com.github.caay2000.ttk.lib.eventbus.impl.KTEventSubscriber
import kotlin.reflect.KClass

class ApplicationConfiguration {

    val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

    val commandBus = CommandBusImpl()
    private val eventPublisher = EventPublisherImpl()

    private val database: InMemoryDatabase = InMemoryDatabase()
    val eventRepository: EventRepository = InMemoryEventRepository(database)

    init {
        KTEventBus.init<Command, Event>()

        instantiateCommandHandler(UpdateDateTimeCommand::class, UpdateDateTimeCommandHandler(dateTimeProvider))
        instantiateCommandHandler(AuditVehicleCommand::class, AuditVehicleCommandHandler(eventRepository))

        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedEventSubscriber(commandBus))

        WorldContextConfiguration(commandBus, eventPublisher, database)
        VehicleContextConfiguration(commandBus, eventPublisher, database)
    }
}

internal fun <T : Command> instantiateCommandHandler(clazz: KClass<T>, commandHandler: CommandHandler<T>): KTCommandHandler<T> =
    object : KTCommandHandler<T>(clazz) {
        override fun invoke(command: T) = commandHandler.invoke(command)
    }

internal fun <T : Event> instantiateEventSubscriber(clazz: KClass<T>, eventSubscriber: EventSubscriber<T>): KTEventSubscriber<T> =
    object : KTEventSubscriber<T>(clazz) {
        override fun handle(event: T) = eventSubscriber.handle(event)
    }
