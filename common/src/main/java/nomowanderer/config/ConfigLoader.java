package nomowanderer.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Platform-agnostic configuration loader
 */
public class ConfigLoader {
    private static final Map<String, ConfigManager> configs = new HashMap<>();
    
    public static void registerConfig(String modId, ConfigSpec spec) {
        Path configDir = getConfigDirectory();
        ConfigManager manager = ConfigManager.create(modId, spec, configDir);
        configs.put(modId, manager);
    }
    
    public static ConfigManager getConfig(String modId) {
        return configs.get(modId);
    }
    
    public static void saveAll() {
        configs.values().forEach(ConfigManager::save);
    }
    
    private static Path getConfigDirectory() {
        // Try to determine the config directory based on the platform
        String userHome = System.getProperty("user.home");
        String configDirProperty = System.getProperty("configDir");
        
        if (configDirProperty != null) {
            return Paths.get(configDirProperty);
        }
        
        // Default minecraft config locations
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return Paths.get(userHome, "AppData", "Roaming", ".minecraft", "config");
        } else if (os.contains("mac")) {
            return Paths.get(userHome, "Library", "Application Support", "minecraft", "config");
        } else {
            return Paths.get(userHome, ".minecraft", "config");
        }
    }
}