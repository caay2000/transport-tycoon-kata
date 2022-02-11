package com.github.caay2000.ttk.context.world.domain

import java.util.UUID

data class Cargo(val origin: Location = Location.FACTORY, val destination: Location) {

    val id: UUID = UUID.randomUUID()
}
