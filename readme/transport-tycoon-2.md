[Back to index](../README.md) | [< Previous Exercise](transport-tycoon-1.md) | [Next Exercise >](transport-tycoon-3.md)

# Exercise 2

Our current solution can be improved. We want to be a bit more realistic and consider the time a vehicle takes to
load/unload any cargo. 

Wa also want to do some modification to our ship, so it can carry more cargo but it has also some speed implications.

## Task

- Add a new rules to the code:

  
  - **Trucks continue with the same capacity and speed**:

    - But takes 1 hour to load *all* cargo
    - Truck also takes a hour to unload *all* cargo


  - **Ship can take up to 4 containers, but is slower now**:
    - Ship takes 2 hours to load *all* cargo
    - Ship takes 2 hours to unload *all* cargo
    - Ship takes 6 hours to travel 4 distance units
    - Note, that ship doesn't wait to be full in order to DEPART. It just LOADs the available cargo and leaves.

[Back to index](../README.md) | [< Previous Exercise](transport-tycoon-1.md) | [Next Exercise >](transport-tycoon-3.md)