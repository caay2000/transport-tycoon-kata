package com.github.caay2000.ttk.context.world.stop.secondary.database

import arrow.core.Either
import arrow.core.Option
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.world.stop.domain.Stop
import com.github.caay2000.ttk.context.world.stop.domain.StopRepository
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryStopRepository(private val database: InMemoryDatabase) : StopRepository {

    override fun save(stop: Stop): Either<Throwable, Stop> = Either.catch { database.save(DATABASE_TABLE, stop.id.rawId, stop) as Stop }
    override fun get(id: StopId): Option<Stop> = Option.catch { database.getById(DATABASE_TABLE, id.rawId) as Stop }
    override fun exists(id: StopId): Boolean = database.exists(DATABASE_TABLE, id.rawId)

    override fun findAllByWorldId(id: WorldId): Set<Stop> =
        database.getAll(DATABASE_TABLE)
            .map { it as Stop }
            .filter { it.worldId == id }
            .toSet()

    companion object {
        private const val DATABASE_TABLE = "WORLD_CTX_stop"
    }
}
