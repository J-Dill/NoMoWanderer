package nomowanderer.config.impl;

import nomowanderer.config.ConfigBuilder;

/**
 * Boolean configuration value.
 */
public class SimpleBooleanConfigValue extends SimpleConfigValue<Boolean> implements ConfigBuilder.BooleanConfigValue {

    public SimpleBooleanConfigValue(String name, String comment, Boolean defaultValue) {
        super(name, comment, defaultValue);
    }
}
