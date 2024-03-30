package nomowanderer;

import com.illusivesoulworks.spectrelib.config.SpectreConfig;
import com.illusivesoulworks.spectrelib.config.SpectreConfigLoader;

public class NoMoWandererCommonMod {

    public static void init() {
        CommonRegistry.init();
    }

    public static void initConfig() {
        SpectreConfigLoader.add(SpectreConfig.Type.SERVER, Config.SERVER_CONFIG,
                NoMoWandererConstants.MODID);
    }

}
