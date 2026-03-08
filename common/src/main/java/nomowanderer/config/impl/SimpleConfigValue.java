package nomowanderer.config.impl;

import nomowanderer.config.ConfigValue;

/**
 * Basic implementation of ConfigValue.
 *
 * @param <T> the type of the configuration value
 */
public class SimpleConfigValue<T> implements ConfigValue<T> {
    private final String name;
    private final String comment;
    private final T defaultValue;
    private T currentValue;

    public SimpleConfigValue(String name, String comment, T defaultValue) {
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
    }

    @Override
    public T get() {
        return currentValue;
    }

    @Override
    public void set(T value) {
        this.currentValue = value;
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " = " + currentValue;
    }
}
