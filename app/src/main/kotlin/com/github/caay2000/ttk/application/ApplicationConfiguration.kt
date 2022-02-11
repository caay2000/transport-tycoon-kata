package com.github.caay2000.ttk.application

import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.audit.domain.service.VehicleAuditorService
import com.github.caay2000.ttk.context.audit.inbound.VehicleEventSubscriber
import com.github.caay2000.ttk.context.audit.outbound.InMemoryEventRepository
import com.github.caay2000.ttk.context.core.command.Command
import com.github.caay2000.ttk.context.core.command.CommandBusImpl
import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.DateTimeProvider
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.core.event.EventPublisherImpl
import com.github.caay2000.ttk.context.core.event.EventSubscriber
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommand
import com.github.caay2000.ttk.context.time.application.UpdateDateTimeCommandHandler
import com.github.caay2000.ttk.context.time.domain.service.DateTimeUpdaterService
import com.github.caay2000.ttk.context.vehicle.domain.VehicleEvent
import com.github.caay2000.ttk.context.world.application.AddCargoCommand
import com.github.caay2000.ttk.context.world.application.AddCargoCommandHandler
import com.github.caay2000.ttk.context.world.application.CreateWorldCommand
import com.github.caay2000.ttk.context.world.application.CreateWorldCommandHandler
import com.github.caay2000.ttk.context.world.application.UpdateWorldCommand
import com.github.caay2000.ttk.context.world.application.UpdateWorldCommandHandler
import com.github.caay2000.ttk.context.world.domain.WorldUpdatedEvent
import com.github.caay2000.ttk.context.world.domain.repository.WorldRepository
import com.github.caay2000.ttk.context.world.domain.service.WorldConfiguratorService
import com.github.caay2000.ttk.context.world.domain.service.WorldUpdaterService
import com.github.caay2000.ttk.context.world.outbound.InMemoryWorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase
import com.github.caay2000.ttk.lib.datetime.DateTimeProviderImpl
import com.github.caay2000.ttk.lib.eventbus.KTCommandHandler
import com.github.caay2000.ttk.lib.eventbus.KTEventBus
import com.github.caay2000.ttk.lib.eventbus.KTEventSubscriber
import kotlin.reflect.KClass
import com.github.caay2000.ttk.context.time.inbound.WorldUpdatedEventSubscriber as WorldUpdatedTimeContextEventSubscriber
import com.github.caay2000.ttk.context.world.inbound.WorldUpdatedEventSubscriber as WorldUpdatedWorldContextEventSubscriber

class ApplicationConfiguration {

    val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

    val commandBus = CommandBusImpl()
    private val eventPublisher = EventPublisherImpl()

    private val database: InMemoryDatabase = InMemoryDatabase()
    val worldRepository: WorldRepository = InMemoryWorldRepository(database)
    val eventRepository: EventRepository = InMemoryEventRepository(database)

    private val worldConfigurator = WorldConfiguratorService(worldRepository)
    private val worldUpdater = WorldUpdaterService(eventPublisher, worldRepository, dateTimeProvider)
    private val vehicleAuditor = VehicleAuditorService(eventRepository)
    private val dateTimeUpdaterService = DateTimeUpdaterService(dateTimeProvider)

    init {
        KTEventBus.init<Command, Event>()

        instantiateCommandHandler(UpdateDateTimeCommand::class, UpdateDateTimeCommandHandler(dateTimeUpdaterService))
        instantiateCommandHandler(CreateWorldCommand::class, CreateWorldCommandHandler(worldConfigurator))
        instantiateCommandHandler(AddCargoCommand::class, AddCargoCommandHandler(worldConfigurator))
        instantiateCommandHandler(UpdateWorldCommand::class, UpdateWorldCommandHandler(worldUpdater))

        instantiateEventSubscriber(VehicleEvent::class, VehicleEventSubscriber(vehicleAuditor))

        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedTimeContextEventSubscriber(commandBus))
        instantiateEventSubscriber(WorldUpdatedEvent::class, WorldUpdatedWorldContextEventSubscriber(commandBus))
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
