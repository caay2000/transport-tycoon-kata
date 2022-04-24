package com.github.caay2000.ttk.application

data class ApplicationWorld(val stops: List<ApplicationStop>, val vehicles: List<ApplicationVehicle>)

data class ApplicationStop(
    val name: String,
    val connections: List<ApplicationConnection>
)

data class ApplicationConnection(
    val targetStop: String,
    val distance: Int,
    val allowedVehicleTypes: Set<String>
)

data class ApplicationVehicle(
    val type: String
)
