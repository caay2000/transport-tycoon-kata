package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommand
import com.github.caay2000.ttk.context.audit.application.AuditVehicleCommandHandler
import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.audit.inbound.VehicleEventSubscriber
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
import com.github.caay2000.ttk.context.vehicle.domain.VehicleEvent
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.handler.AddCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.application.handler.UpdateWorldCommandHandler
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
import com.github.caay2000.ttk.lib.eventbus.KTCommandHandler
import com.github.caay2000.ttk.lib.eventbus.KTEventBus
import com.github.caay2000.ttk.lib.eventbus.KTEventSubscriber
import kotlin.reflect.KClass
import com.github.caay2000.ttk.context.time.inbound.WorldUpdatedEventSubscriber as TimeContextWorldUpdatedEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.WorldUpdatedEventSubscriber as WorldContextWorldUpdatedEventSubscriber

class ApplicationConfiguration {

    val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

    val commandBus = CommandBusImpl()
    private val eventPublisher = EventPublisherImpl()

    private val database: InMemoryDatabase = InMemoryDatabase()
    private val worldRepository: WorldRepository = InMemoryWorldRepository(database)
    val eventRepository: EventRepository = InMemoryEventRepository(database)

    init {
        KTEventBus.init<Command, Event>()

        instantiateCommandHandler(UpdateDateTimeCommand::class, UpdateDateTimeCommandHandler(dateTimeProvider))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(AddCargoCommand::class, AddCargoCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(eventPublisher, worldRepository))
        instantiateCommandHandler(AuditVehicleCommand::class, AuditVehicleCommandHandler(eventRepository))

        instantiateEventSubscriber(WorldUpdatedEvent::class, TimeContextWorldUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldContextWorldUpdatedEventSubscriber(commandBus))
        instantiateEventSubscriber(VehicleEvent::class, VehicleEventSubscriber(commandBus))
    }

    private fun <T : Command> instantiateCommandHandler(clazz: KClass<T>, commandHandler: CommandHandler<T>): KTCommandHandler<T> =
        object : KTCommandHandler<T>(clazz) {
            override fun invoke(command: T) = commandHandler.invoke(command)
        }

    private fun <T : Event> instantiateEventSubscriber(clazz: KClass<T>, eventSubscriber: EventSubscriber<T>): KTEventSubscriber<T> =
        object : KTEventSubscriber<T>(clazz) {
            override fun handle(event: T) = eventSubscriber.handle(event)
        }
}
