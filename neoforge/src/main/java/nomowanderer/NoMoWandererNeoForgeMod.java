package nomowanderer;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import nomowanderer.client.ClientSetup;
import nomowanderer.commands.NoMoWandererBaseCommand;
import nomowanderer.compat.ExternalMods;
import nomowanderer.items.AntiSolicitorTalismanItem;
import nomowanderer.world.EntitySpawnHandler;
import nomowanderer.world.SpawnHandlerResult;
//import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@Mod(NoMoWandererConstants.MODID)
public class NoMoWandererNeoForgeMod {

    public static final boolean CURIOS = ExternalMods.CURIOS.isLoaded();

    public NoMoWandererNeoForgeMod(IEventBus modEventBus) {
        NoMoWandererCommonMod.init();
        NoMoWandererCommonMod.initConfig();

        if (CURIOS) {
            NeoForgeGameTestRegistry.init();
        }

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.init(modEventBus);
        }

        modEventBus.addListener(this::registerTabs);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::handleSpawns);
    }

    public void handleSpawns(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk() || !(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        SpawnHandlerResult result = EntitySpawnHandler.maybeChangeEntitySpawn(
                event.getEntity(),
                serverLevel,
                Optional.of(this::hasAntiSolicitorTalisman));

        if (result == SpawnHandlerResult.CANCELLED) {
            event.setCanceled(true);
        }
    }

    private boolean hasAntiSolicitorTalisman(Player player) {
        if (!CURIOS) {
            return false;
        }

        return false;
//        return CuriosApi.getCuriosInventory(player)
//                .map(inventory -> inventory.findFirstCurio(AntiSolicitorTalismanItem::isEnabled).isPresent())
//                .orElse(false);
    }

    public void registerTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(CommonRegistry.NO_SOLICITING_SIGN_ITEM.get());
            event.accept(CommonRegistry.TRADER_RUG_ITEM.get());
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CommonRegistry.NO_SOLICITING_TALISMAN.get().getDefaultInstance());
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CommonRegistry.TRADER_CLOTH_PIECE_ITEM.get());
        }
    }

    public void registerCommands(RegisterCommandsEvent event) {
        NoMoWandererBaseCommand.create(event.getDispatcher());
    }

}