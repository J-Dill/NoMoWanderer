![NoMoWanderer Banner Image](https://i.imgur.com/2HdZhC2.png)

![Mod Download Total](http://cf.way2muchnoise.eu/full_nomowanderer_downloads.svg)
![MC Versions](http://cf.way2muchnoise.eu/versions/nomowanderer.svg)

Getting tired of those Wandering Traders trampling across your lawn and your crops, just to offer you some measly trades?
Look no further than... well, this mod! Provides the player with a few blocks/items to either prevent or control
Wandering Trader spawns.

## Features
### No Soliciting Sign
Prevents configured entity spawns within a configurable distance (in chunks) of the block.

### Anti-Soliciting Talisman
Item that, when in a Baubles slot, Curios slot, or anywhere in your inventory, will prevent
Wandering Trader spawns around the player. The distance is the same used for the No Soliciting Sign.

### Trader Rug
Provides a way to set the spawn location of Wandering Traders nearby. If a Wandering Trader spawns within the `radius` 
of the rug, it will instead spawn on top of this block.

## Config
### _nomowanderer-common.toml_
`entityWatchList` These entities will be blocked from spawning if within the radius of a No Soliciting Sign.
If the entity is not blocked, its spawn will be moved to a Trader Rug if in the radius of one.
The Wandering Trader and the Plague Doctor from the [Rats](https://www.curseforge.com/minecraft/mc-mods/rats)
mod (sorry alex) are in the list by default.

`radius` The effective range of this mod's items/blocks. Default 8.

`disableSpawns` true to disable all spawn of configured entities in _entityWatchList_. Default false.
