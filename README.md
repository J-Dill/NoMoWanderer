# NoMoWanderer

![Mod Download Total](http://cf.way2muchnoise.eu/full_nomowanderer_downloads.svg)
![MC Versions](http://cf.way2muchnoise.eu/versions/nomowanderer.svg)

Minecraft mod that adds a block and an item to prevent configured entity spawns.

## Config
#### _nomowanderer-common.toml_
`entityBlockList` Entries here, in the form of _modid:entity_name_, will be prevented from spawning
if within range of the No Soliciting Sign or a Totem of Unexchange. The Wandering Trader and the
Plague Doctor from the [Rats](https://www.curseforge.com/minecraft/mc-mods/rats) mod (sorry alex)
are in the list by default.

`radius` The effective range of the No Soliciting Sign, measured in chunks. If a configured entity
attempts to spawn within _radius_ range of a sign, the spawn will be prevented. Default 8.

`disableSpawns` true to disable all spawn of configured entities. Default false.

## No Soliciting Sign
Block that prevents configured entity spawns within a configurable distance (in chunks) of the block.

### Recipe
The current recipe follows that of a normal sign, except you need to put green dye in the bottom
left corner and red dye in the bottom right corner of the crafting grid. You get 3 signs from the craft.

![Sign Recipe](https://i.imgur.com/S5NxqWn.png)

## Totem of Unexchange
Item that, when in a Baubles slot, Curios slot, or anywhere in your inventory, will prevent
configured entity spawns around the player. The distance is the same used for the No Soliciting Sign.

### Recipe
![Totem Recipe](https://i.imgur.com/GqPWimM.png)
