package nomowanderer;

import net.minecraft.resources.ResourceLocation;
import nomowanderer.config.SimpleConfig;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Config {

    private static SimpleConfig config;
    private static boolean loaded = false;

    // Config values with defaults
    public static boolean DISABLE_ENTITY_SPAWNS = false;
    public static List<String> ENTITY_WATCH_LIST = Arrays.asList(
        "minecraft:wandering_trader", 
        "minecraft:trader_llama", 
        "rats:plague_doctor", 
        "supplementaries:red_merchant"
    );
    public static int RUG_WATCH_RADIUS = 6;
    public static int SIGN_WATCH_RADIUS = 6;
    public static int SPAWN_CAP_WATCH_RADIUS = 6;
    public static int TALISMAN_WATCH_RADIUS = 6;
    public static int ENTITY_SPAWN_CAP = 0;

    public static void load(Path configDir) {
        if (loaded) return;
        
        Path configFile = configDir.resolve("nomowanderer-common.toml");
        config = new SimpleConfig(configFile, NoMoWandererConstants.MODID);
        config.load();
        
        loadValues();
        config.save(); // Save to create file with defaults if it doesn't exist
        loaded = true;
    }

    private static void loadValues() {
        // General config
        DISABLE_ENTITY_SPAWNS = config.getBoolean("general.disableSpawns", false);
        ENTITY_WATCH_LIST = config.getStringList("general.entityWatchList", Arrays.asList(
            "minecraft:wandering_trader", 
            "minecraft:trader_llama", 
            "rats:plague_doctor", 
            "supplementaries:red_merchant"
        ));
        
        // Sign config
        SIGN_WATCH_RADIUS = config.getInt("noSolicitingSign.signRadius", 6, 1, 12);
        
        // Spawn cap config  
        SPAWN_CAP_WATCH_RADIUS = config.getInt("spawnCap.spawnCapRadius", 6, 1, 12);
        ENTITY_SPAWN_CAP = config.getInt("spawnCap.spawnCap", 0, 0, Integer.MAX_VALUE);
        
        // Talisman config
        TALISMAN_WATCH_RADIUS = config.getInt("talisman.talismanRadius", 6, 1, 12);
        
        // Trader rug config
        RUG_WATCH_RADIUS = config.getInt("traderRug.rugRadius", 6, 1, 12);
    }

}
