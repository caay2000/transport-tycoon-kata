package com.github.caay2000.ttk.context.core.domain

import java.util.UUID
import kotlin.reflect.full.primaryConstructor

sealed class DomainId(uuid: UUID) {
    abstract val id: UUID
    val rawId = uuid.toString()
}

data class EventId(override val id: UUID = UUID.randomUUID()) : DomainId(id)
data class WorldId(override val id: UUID = UUID.randomUUID()) : DomainId(id)
data class StopId(override val id: UUID = UUID.randomUUID()) : DomainId(id)
data class VehicleId(override val id: UUID = UUID.randomUUID()) : DomainId(id)

inline fun <reified T : DomainId> UUID.toDomainId() =
    T::class.primaryConstructor!!.call(this)
