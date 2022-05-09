package com.github.caay2000.ttk.context.shared.domain

import java.util.UUID
import kotlin.reflect.full.primaryConstructor

sealed class DomainId(uuid: UUID) {
    abstract val uuid: UUID
    val rawId = uuid.toString()
}

data class EventId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)
data class WorldId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)
data class StopId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)
data class VehicleId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)
data class CargoId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)
data class ProgressId(override val uuid: UUID = UUID.randomUUID()) : DomainId(uuid)

inline fun <reified T : DomainId> randomDomainId() =
    UUID.randomUUID().toDomainId<T>()

inline fun <reified T : DomainId> UUID.toDomainId() =
    T::class.primaryConstructor!!.call(this)
