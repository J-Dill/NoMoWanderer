package nomowanderer.config;

/**
 * Global holder for the platform-specific ConfigManager implementation.
 * This is set by platform-specific startup code.
 */
public class ConfigManagerHolder {
    private static ConfigManager instance;

    public static void setInstance(ConfigManager manager) {
        if (instance != null) {
            throw new IllegalStateException("ConfigManager instance already set");
        }
        instance = manager;
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConfigManager instance not initialized. " +
                "Make sure platform-specific mod initialization is called.");
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    // For testing purposes
    static void reset() {
        instance = null;
    }
}

