package nomowanderer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import nomowanderer.client.ClientSetup;
import nomowanderer.commands.NoMoWandererBaseCommand;
import nomowanderer.config.ConfigManagerHolder;
import nomowanderer.config.ForgeConfigManager;
import nomowanderer.world.EntitySpawnHandler;
import nomowanderer.world.SpawnHandlerResult;

import java.lang.invoke.MethodHandles;
import java.util.Optional;


@Mod(NoMoWandererConstants.MODID)
public class NoMoWandererForgeMod {

    public NoMoWandererForgeMod(FMLJavaModLoadingContext context) {
        ConfigManagerHolder.setInstance(new ForgeConfigManager());
        NoMoWandererCommonMod.init();
        NoMoWandererCommonMod.initConfig();
        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.init(context);
        }

        BusGroup.DEFAULT.register(MethodHandles.lookup(), this);
        BusGroup modBusGroup = context.getModBusGroup();
        BuildCreativeModeTabContentsEvent.getBus(modBusGroup).addListener(this::registerTabs);
    }

    @SubscribeEvent
    public boolean handleSpawns(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && event.getLevel() instanceof ServerLevel level) {
            SpawnHandlerResult result = EntitySpawnHandler.maybeChangeEntitySpawn(
                    event.getEntity(),
                    level,
                    Optional.empty());
            return SpawnHandlerResult.CANCELLED.equals(result);
        }
        return false;
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        NoMoWandererBaseCommand.create(event.getDispatcher());
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

}