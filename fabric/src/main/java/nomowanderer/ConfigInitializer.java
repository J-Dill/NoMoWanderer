package nomowanderer;

import net.fabricmc.api.ModInitializer;
import nomowanderer.config.ConfigManagerHolder;
import nomowanderer.config.FabricConfigManager;

public class ConfigInitializer implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigManagerHolder.setInstance(new FabricConfigManager());
        NoMoWandererCommonMod.initConfig();
    }

}
