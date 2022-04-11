package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.shared.domain.Location
import java.util.UUID

data class Cargo(val origin: Location = Location.FACTORY, val destination: Location) {

    val id: UUID = UUID.randomUUID()
}
