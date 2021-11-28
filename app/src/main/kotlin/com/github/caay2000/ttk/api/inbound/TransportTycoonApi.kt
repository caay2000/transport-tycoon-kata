package com.github.caay2000.ttk.api.inbound

interface TransportTycoonApi {

    fun execute(deliveries: List<Delivery>): Steps
}
