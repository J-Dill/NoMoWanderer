package nomowanderer;

import com.illusivesoulworks.spectrelib.config.SpectreConfigInitializer;

public class ConfigInitializer implements SpectreConfigInitializer {

    @Override
    public void onInitializeConfig() {
        NoMoWandererCommonMod.initConfig();
    }

}
