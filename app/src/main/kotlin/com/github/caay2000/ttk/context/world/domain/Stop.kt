package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.CargoId
import com.github.caay2000.ttk.context.core.domain.StopId
import com.github.caay2000.ttk.context.core.domain.randomDomainId
import com.github.caay2000.ttk.context.core.domain.toDomainId
import java.util.UUID

data class Stop(val id: StopId = UUID.randomUUID().toDomainId(), val location: Location) {

    companion object {
        fun get(location: Location) = stops[location]!!
        fun all() = stops.values.toList()
        fun cleanAll() = stops.forEach {
            it.value._cargo.clear()
        }

        private val stops = mapOf(
            Location.FACTORY to Stop(randomDomainId(), Location.FACTORY),
            Location.PORT to Stop(randomDomainId(), Location.PORT),
            Location.WAREHOUSE_A to Stop(randomDomainId(), Location.WAREHOUSE_A),
            Location.WAREHOUSE_B to Stop(randomDomainId(), Location.WAREHOUSE_B)
        )
    }

    private val _cargo = mutableListOf<Cargo>()
    val cargo: List<Cargo>
        get() = _cargo.toList()

    fun hasCargo(): Boolean = _cargo.isEmpty().not()

    fun addCargo(cargo: Cargo): Stop {
        _cargo.add(cargo)
        return this
    }

    fun removeCargo(cargoId: CargoId): Boolean = _cargo.removeIf { it.id == cargoId.uuid }

    fun distanceTo(stop: Stop) = this.location.distanceTo(stop.location)
}
