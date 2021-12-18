package com.github.caay2000.ttk.api.inbound

import com.github.caay2000.ttk.domain.Location
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class Cargo(val destination: Location) {

    @Transient
    val id: UUID = UUID.randomUUID()
}
