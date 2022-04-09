package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommand
import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommandHandler
import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.audit.outbound.InMemoryEventRepository
import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBusImpl
import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.core.event.EventPublisherImpl
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.core.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommand
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommandHandler
import com.github.caay2000.ttk.context.time.inbound.WorldUpdatedEventSubscriber
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
import com.github.caay2000.ttk.lib.eventbus.KTCommandHandler
import com.github.caay2000.ttk.lib.eventbus.KTEventBus
import com.github.caay2000.ttk.lib.eventbus.KTEventSubscriber
import kotlin.reflect.KClass

class ApplicationConfiguration {

    val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

    val commandBus = CommandBusImpl()
    private val eventPublisher = EventPublisherImpl()

    private val database: InMemoryDatabase = InMemoryDatabase()
    val eventRepository: EventRepository = InMemoryEventRepository(database)

    init {
        KTEventBus.init<Command, Event>()

        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedEventSubscriber(commandBus))
        instantiateCommandHandler(UpdateDateTimeCommand::class, UpdateDateTimeCommandHandler(dateTimeProvider))
        instantiateCommandHandler(AuditVehicleCommand::class, AuditVehicleCommandHandler(eventRepository))

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
