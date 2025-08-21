package nomowanderer;

import net.fabricmc.loader.api.FabricLoader;

public class FabricConfigInitializer {

    public static void initializeConfig() {
        NoMoWandererCommonMod.initConfig(FabricLoader.getInstance().getConfigDir());
    }

}
