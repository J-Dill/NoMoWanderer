package nomowanderer.config;

import java.util.Map;

/**
 * Represents a complete configuration specification for a mod.
 * This can be used by platform-specific implementations to generate
 * configuration files and handle persistence.
 */
public interface ConfigSpec {

    /**
     * Gets all configuration values in this spec, organized by category.
     *
     * @return a map of category names to their configuration values
     */
    Map<String, Map<String, ConfigValue<?>>> getValues();

    /**
     * Gets the name of this configuration spec (typically the mod ID).
     *
     * @return the spec name
     */
    String getName();

    /**
     * Gets the type of this configuration spec (e.g., SERVER, CLIENT).
     *
     * @return the configuration type
     */
    ConfigType getType();

    /**
     * Resets all values to their defaults.
     */
    void reset();

    enum ConfigType {
        SERVER,
        CLIENT,
        COMMON
    }
}

