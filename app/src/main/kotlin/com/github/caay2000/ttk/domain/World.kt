package com.github.caay2000.ttk.domain

data class World private constructor(val vehicles: List<Vehicle>) {
    constructor(vehicle: Vehicle, vararg vehicles: Vehicle) : this(listOf(vehicle) + listOf(*vehicles))
}
