![NoMoWanderer Banner Image](https://i.imgur.com/2HdZhC2.png)

![Mod Download Total](http://cf.way2muchnoise.eu/full_nomowanderer_downloads.svg)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/BAGslLB8?logo=modrinth&color=lime&label=Modrinth%20Downloads)
![MC Versions](http://cf.way2muchnoise.eu/versions/nomowanderer.svg)

Getting tired of those Wandering Traders trampling across your lawn and your crops, just to offer you some measly trades?
Look no further than... well, this mod! Provides the player with a few blocks/items to prevent and/or control
Wandering Trader spawns.

## Features

### No Soliciting Sign
Prevents configured entity spawns within a configurable distance (in chunks) of the block.

### Anti-Soliciting Talisman
Item that, when in a Baubles slot, Curios slot, or anywhere in your inventory, will prevent
Wandering Trader spawns around the player. The distance is the same used for the No Soliciting Sign.

It can be toggled on/off by _Shift right-clicking_ the Talisman.

### Trader Rug
Provides a way to set the spawn location of Wandering Traders nearby. If a Wandering Trader spawns within the `radius` 
of the rug, it will instead spawn on top of this block.

## Config

### _nomowanderer-common.toml_
`entityWatchList` These entities will be blocked from spawning if within the radius of a No Soliciting Sign.
If the entity is not blocked, its spawn will be moved to a Trader Rug if in the radius of one.
The Wandering Trader, Plague Doctor from the [Rats](https://www.curseforge.com/minecraft/mc-mods/rats)
mod, and Red Merchant from [Supplementaries](https://www.curseforge.com/minecraft/mc-mods/supplementaries) 
are in this list by default.

`radius` The effective range of this mod's items/blocks. Default 8.

`disableSpawns` true to disable all spawn of configured entities in _entityWatchList_. Default false.

## Development

### Test World Setup

To facilitate manual testing of crafting recipes, rendering, and server functionality across all supported platforms (NeoForge, Forge, Fabric), you can set up a shared test world template.

#### Setup
1. Create a Minecraft world with your desired test setup (pre-placed No Soliciting Signs, Trader Rugs, crafting stations, etc.).
2. Copy the world save folder to `common/test_world_template/` in your local project directory.
3. Run the following command to copy the template to all platform run directories:
   ```
   .\gradlew copyTestWorld
   ```
4. Launch the client or server for your desired platform and load the `test_world` save.

#### What it should include
- Pre-placed No Soliciting Signs, Trader Rugs, and other mod blocks for spawn testing.
- Crafting stations set up for recipe verification.
- Configured areas to test spawn prevention and redirection.

#### Notes
- The template is not committed to the repository for privacy reasons (to avoid including personal player data).
- The Gradle task ensures a fresh copy each time, overriding any existing test world.
- You can modify the template locally as needed for your testing scenarios.
