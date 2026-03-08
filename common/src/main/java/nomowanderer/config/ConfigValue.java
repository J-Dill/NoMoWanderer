package nomowanderer.config;

/**
 * Represents a single configuration value with optional validation.
 *
 * @param <T> the type of the configuration value
 */
public interface ConfigValue<T> {

    /**
     * Gets the current value of this configuration option.
     *
     * @return the current value
     */
    T get();

    /**
     * Sets the value of this configuration option.
     *
     * @param value the new value
     */
    void set(T value);

    /**
     * Gets the default value of this configuration option.
     *
     * @return the default value
     */
    T getDefault();

    /**
     * Gets the comment/description for this configuration option.
     *
     * @return the comment
     */
    String getComment();

    /**
     * Gets the name/path of this configuration option.
     *
     * @return the name
     */
    String getName();
}

