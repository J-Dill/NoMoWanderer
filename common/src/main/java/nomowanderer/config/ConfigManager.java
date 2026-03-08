package nomowanderer.config;

/**
 * Platform-agnostic service for managing configuration lifecycle.
 * Platform-specific implementations should be registered via service loader or injected.
 */
public interface ConfigManager {

    /**
     * Registers a configuration spec for the given mod ID.
     * This handles platform-specific serialization and persistence.
     *
     * @param spec the configuration spec to register
     * @param modId the mod ID
     */
    void registerConfig(ConfigSpec spec, String modId);

    /**
     * Loads configuration from disk.
     *
     * @param modId the mod ID
     */
    void loadConfig(String modId);

    /**
     * Gets a previously registered configuration spec.
     *
     * @param modId the mod ID
     * @return the configuration spec, or null if not registered
     */
    ConfigSpec getConfig(String modId);

    /**
     * Reloads all registered configurations from disk.
     */
    void reloadAll();
}

