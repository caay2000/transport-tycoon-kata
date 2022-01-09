package com.github.caay2000.ttk.api.inbound

import com.github.caay2000.ttk.domain.Location
import com.github.caay2000.ttk.domain.Vehicle
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KClass

sealed class Event(
    val id: UUID,
    val time: LocalDateTime
)

data class DepartEvent(
    val vehicleId: UUID,
    val type: KClass<out Vehicle>,
    val location: Location,
    val destination: Location,
    val cargo: Cargo
) : Event(UUID.randomUUID(), LocalDateTime.now())

data class ArriveEvent(
    val vehicleId: UUID,
    val type: KClass<out Vehicle>,
    val location: Location,
    val cargo: Cargo
) : Event(UUID.randomUUID(), LocalDateTime.now())
