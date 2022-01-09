package com.github.caay2000.ttk.api.inbound

interface TransportTycoonApi {

    fun execute(cargo: List<Cargo>): Int
}
