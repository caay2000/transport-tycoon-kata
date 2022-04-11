package com.github.caay2000.ttk.context.vehicle.outbound

import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.application.repository.StopRepository
import com.github.caay2000.ttk.context.vehicle.domain.world.Stop
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryStopRepository(private val database: InMemoryDatabase) : StopRepository {

    // TODO create a DTO object for stop

    override fun save(stop: Stop) = Either.catch { database.save(DATABASE_TABLE, stop.id.rawId, stop) as Stop }
    override fun get(id: StopId) = (database.getById(DATABASE_TABLE, id.rawId) as Stop).toOption()
    override fun exists(id: StopId): Boolean = database.exists(DATABASE_TABLE, id.rawId)
    override fun findByName(name: String): Option<Stop> = Option.catch {
        database.getAll(DATABASE_TABLE)
            .map { it as Stop }
            .first { it.name == name }
    }

    fun findAllByWorldId(worldId: WorldId): List<Stop> = database.getAll(DATABASE_TABLE)
        .map { it as Stop }
        .filter { it.worldId == worldId }

    companion object {
        private const val DATABASE_TABLE = "VEHICLE_CTX_stop"
    }
}
