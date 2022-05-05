package com.github.caay2000.ttk.lib.eventbus.impl

class KTQueryExecutor {

    fun <QUERY, RESPONSE> execute(query: QUERY): RESPONSE = KTEventBus.getInstance<Any, QUERY, Any>().publishQuery(query)
}
