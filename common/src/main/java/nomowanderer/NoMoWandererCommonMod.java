package nomowanderer;

import nomowanderer.config.ConfigManagerHolder;

public class NoMoWandererCommonMod {

    public static void init() {
        CommonRegistry.init();
        CommonGameTestRegistry.init();
    }

    public static void initConfig() {
        ConfigManagerHolder.getInstance().registerConfig(Config.SERVER_CONFIG, NoMoWandererConstants.MODID);
    }

}
