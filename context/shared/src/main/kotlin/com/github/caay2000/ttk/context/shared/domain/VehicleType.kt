package com.github.caay2000.ttk.context.shared.domain

enum class VehicleType {

    TRUCK,
    BOAT
}

fun String.toVehicleType() = VehicleType.valueOf(this)
