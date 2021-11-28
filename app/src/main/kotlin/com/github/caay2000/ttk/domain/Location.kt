package com.github.caay2000.ttk.domain

enum class Location {
    FACTORY,
    PORT,
    WAREHOUSE_A,
    WAREHOUSE_B;

    fun distanceTo(destination: Location): Distance =
        if (this == destination) 0
        else distances[Pair(this, destination)] ?: distances[Pair(destination, this)] ?: -1
}

private val distances = mapOf(
    Pair(Pair(Location.FACTORY, Location.WAREHOUSE_B), 5),
    Pair(Pair(Location.FACTORY, Location.PORT), 1),
    Pair(Pair(Location.PORT, Location.WAREHOUSE_A), 4)
)
