package nomowanderer;

import nomowanderer.config.ConfigLoader;

public class NoMoWandererCommonMod {

    public static void init() {
        CommonRegistry.init();
        CommonGameTestRegistry.init();
    }

    public static void initConfig() {
        ConfigLoader.registerConfig(NoMoWandererConstants.MODID, Config.SERVER_CONFIG);
    }

}
