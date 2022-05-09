package com.github.caay2000.ttk.context.shared.domain

data class Progress(val progressId: ProgressId, val total: Int) {

    private var done: Int = 0

    val completed: Boolean
        get() = total == done

    fun increase(increase: Int) {
        done = +increase
    }
}
