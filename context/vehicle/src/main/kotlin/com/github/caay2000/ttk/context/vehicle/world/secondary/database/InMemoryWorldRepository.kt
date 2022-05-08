package com.github.caay2000.ttk.context.vehicle.world.secondary.database

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatten
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.world.domain.World
import com.github.caay2000.ttk.context.vehicle.world.domain.WorldRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryWorldRepository(private val database: InMemoryDatabase) : WorldRepository {

    // TODO create a DTO object for world

    override fun save(world: World) = Either.catch {
        database.save(DATABASE_TABLE, world.id.rawId, world) as World
    }

    override fun get(id: WorldId): Option<World> =
        Option.catch {
            database.getById(DATABASE_TABLE, id.rawId) as World
        }

    override fun exists(id: WorldId): Boolean = database.exists(DATABASE_TABLE, id.rawId)

    override fun getStop(worldId: WorldId, stopId: StopId) =
        Option.catch {
            this.get(worldId)
                .flatMap { world -> world.stops.first { it.id == stopId }.toOption() }
        }.flatten()

    companion object {
        private const val DATABASE_TABLE = "VEHICLE_CTX_world"
    }
}
