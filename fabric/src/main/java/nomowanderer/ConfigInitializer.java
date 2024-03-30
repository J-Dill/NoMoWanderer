package nomowanderer;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;

public class ConfigInitializer implements SpectreLibInitializer {

    @Override
    public void onInitializeConfig() {
        NoMoWandererCommonMod.initConfig();
    }

}
