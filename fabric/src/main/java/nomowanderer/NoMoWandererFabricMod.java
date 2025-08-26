package nomowanderer;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTabs;
import nomowanderer.commands.NoMoWandererBaseCommand;
import nomowanderer.world.EntitySpawnHandler;
import nomowanderer.world.SpawnHandlerResult;

import java.util.Optional;

public class NoMoWandererFabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        NoMoWandererCommonMod.init();
        NoMoWandererCommonMod.initConfig();
        registerCommands();
        registerEntitySpawnWatching();
        registerCreativeTabs();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dis, reg, env) -> {
            dis.register(NoMoWandererBaseCommand.create(dis));
        });
    }

    private static void registerEntitySpawnWatching() {
        ServerEntityEvents.ENTITY_LOAD.register(((entity, level) -> {
            SpawnHandlerResult result = EntitySpawnHandler.maybeChangeEntitySpawn(entity, level, Optional.empty());
            if (SpawnHandlerResult.CANCELLED.equals(result)) {
                if (!entity.isRemoved()) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }));
    }

    private static void registerCreativeTabs() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.accept(CommonRegistry.NO_SOLICITING_SIGN_ITEM.get());
            entries.accept(CommonRegistry.TRADER_RUG_ITEM.get());
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(CommonRegistry.NO_SOLICITING_TALISMAN.get().getDefaultInstance());
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(CommonRegistry.TRADER_CLOTH_PIECE_ITEM.get());
        });
    }
}