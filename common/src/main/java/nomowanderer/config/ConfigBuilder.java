package nomowanderer.config;

import java.util.List;
import java.util.function.Predicate;

/**
 * Builder for constructing configuration specifications.
 * Use this to programmatically define configuration options.
 */
public interface ConfigBuilder {

    /**
     * Pushes a new configuration category.
     *
     * @param category the category name
     * @return this builder for chaining
     */
    ConfigBuilder push(String category);

    /**
     * Pops the current configuration category.
     *
     * @return this builder for chaining
     */
    ConfigBuilder pop();

    /**
     * Adds a comment to the next configuration value.
     *
     * @param comments the comment lines
     * @return this builder for chaining
     */
    ConfigBuilder comment(String... comments);

    /**
     * Defines a boolean configuration value with a default.
     *
     * @param name the configuration name
     * @param defaultValue the default value
     * @return a ConfigValue for this boolean
     */
    BooleanConfigValue define(String name, boolean defaultValue);

    /**
     * Defines an integer configuration value within a range.
     *
     * @param name the configuration name
     * @param defaultValue the default value
     * @param minValue the minimum allowed value
     * @param maxValue the maximum allowed value
     * @return a ConfigValue for this integer
     */
    IntConfigValue defineInRange(String name, int defaultValue, int minValue, int maxValue);

    /**
     * Defines a list configuration value with validation.
     *
     * @param name the configuration name
     * @param defaultValue the default list
     * @param validator a predicate to validate each element
     * @param <T> the type of list elements
     * @return a ConfigValue for this list
     */
    <T> ConfigValue<List<T>> defineList(String name, List<T> defaultValue, Predicate<Object> validator);

    /**
     * Defines a generic configuration value.
     *
     * @param name the configuration name
     * @param defaultValue the default value
     * @param <T> the type of the value
     * @return a ConfigValue for this generic value
     */
    <T> ConfigValue<T> define(String name, T defaultValue);

    /**
     * Builds the final ConfigSpec from this builder.
     *
     * @return the built ConfigSpec
     */
    ConfigSpec build();

    /**
     * Interface for boolean configuration values.
     */
    interface BooleanConfigValue extends ConfigValue<Boolean> {
    }

    /**
     * Interface for integer configuration values with range constraints.
     */
    interface IntConfigValue extends ConfigValue<Integer> {
        int getMin();

        int getMax();
    }
}

