package com.github.caay2000.ttk.api.inbound

import com.github.caay2000.ttk.domain.Location
import java.util.UUID

data class Cargo(val destination: Location) {

    val id: UUID = UUID.randomUUID()
    val origin: Location = Location.FACTORY
}
