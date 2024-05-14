package nomowanderer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.client.ClientSetup;
import nomowanderer.compat.ExternalMods;
import nomowanderer.items.AntiSolicitorTalismanItem;
import nomowanderer.util.SpawnTraderCommand;
import nomowanderer.world.EntitySpawnHandler;
import nomowanderer.world.SpawnHandlerResult;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;
import java.util.function.Predicate;


@Mod(NoMoWandererConstants.MODID)
public class NoMoWandererForgeMod {

    public static final boolean CURIOS = ExternalMods.CURIOS.isLoaded();

    public NoMoWandererForgeMod() {
        NoMoWandererCommonMod.init();
        NoMoWandererCommonMod.initConfig();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::init);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::registerTabs);

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::handleSpawns);
    }

    public void handleSpawns(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && event.getLevel() instanceof ServerLevel level) {
            Predicate<Player> invCheck = (player) ->
                    CURIOS && CuriosApi.getCuriosHelper().findFirstCurio(player, AntiSolicitorTalismanItem::isEnabled).isPresent();
            SpawnHandlerResult result = EntitySpawnHandler.maybeChangeEntitySpawn(
                    event.getEntity(),
                    level,
                    Optional.of(invCheck));
            if (SpawnHandlerResult.CANCELLED.equals(result)) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                }
                event.setResult(Event.Result.DENY);
            }
        }
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
        SpawnTraderCommand.create(event.getDispatcher());
    }

}