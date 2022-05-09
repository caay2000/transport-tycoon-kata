package com.github.caay2000.ttk.context.shared.domain

enum class VehicleTypeEnum {

    TRUCK,
    BOAT
}

fun String.toVehicleType() = VehicleTypeEnum.valueOf(this)
