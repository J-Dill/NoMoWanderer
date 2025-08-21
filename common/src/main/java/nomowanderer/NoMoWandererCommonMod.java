package nomowanderer;

import java.nio.file.Path;

public class NoMoWandererCommonMod {

    public static void init() {
        CommonRegistry.init();
    }

    public static void initConfig(Path configDir) {
        Config.load(configDir);
    }

}
