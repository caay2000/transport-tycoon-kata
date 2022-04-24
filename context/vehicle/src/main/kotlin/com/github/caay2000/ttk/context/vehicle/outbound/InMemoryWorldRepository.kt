package com.github.caay2000.ttk.context.vehicle.outbound

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.domain.repository.WorldRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.World
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryWorldRepository(private val database: InMemoryDatabase) : WorldRepository {

    private val stopRepository = InMemoryStopRepository(database)

    // TODO create a DTO object for world

    override fun save(world: World) = Either.catch {
        world.stops.forEach { stop ->
            stopRepository.save(stop)
        }
        database.save(DATABASE_TABLE, world.id.rawId, world.copy(stops = emptySet())) as World
    }

    override fun get(id: WorldId): Option<World> =
        Option.catch {
            val world = database.getById(DATABASE_TABLE, id.rawId) as World
            val stops = stopRepository.findAllByWorldId(id)
            world.copy(stops = stops.toSet())
        }

    override fun exists(id: WorldId): Boolean = database.exists(DATABASE_TABLE, id.rawId)

    override fun getStop(stopId: StopId) = stopRepository.get(stopId)

    companion object {
        private const val DATABASE_TABLE = "VEHICLE_CTX_world"
    }
}
