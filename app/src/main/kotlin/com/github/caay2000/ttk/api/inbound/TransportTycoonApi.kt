package com.github.caay2000.ttk.api.inbound

interface TransportTycoonApi {

    fun calculateSteps(destinations: List<Destination>): Steps
}
