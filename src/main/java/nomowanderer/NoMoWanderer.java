package nomowanderer;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(NoMoWanderer.MODID)
public class NoMoWanderer {

    public static final String MODID = "nomowanderer";

    private static final Logger LOGGER = LogManager.getLogger(NoMoWanderer.MODID + " Mod Event Subscriber");

    public NoMoWanderer() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("nomowanderer-common.toml"));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.BLOCKS.register(modEventBus);
        Registry.ITEMS.register(modEventBus);
        Registry.BLOCK_ENTITIES.register(modEventBus);
    }

}