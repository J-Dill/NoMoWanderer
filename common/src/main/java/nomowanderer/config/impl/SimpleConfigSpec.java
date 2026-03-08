package nomowanderer.config.impl;

import nomowanderer.config.ConfigSpec;
import nomowanderer.config.ConfigValue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Basic implementation of ConfigSpec.
 */
public class SimpleConfigSpec implements ConfigSpec {
    private final String name;
    private final ConfigType type;
    private final Map<String, Map<String, ConfigValue<?>>> values;

    public SimpleConfigSpec(String name, ConfigType type, Map<String, Map<String, ConfigValue<?>>> values) {
        this.name = name;
        this.type = type;
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    @Override
    public Map<String, Map<String, ConfigValue<?>>> getValues() {
        return values;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConfigType getType() {
        return type;
    }

    @Override
    public void reset() {
        for (Map<String, ConfigValue<?>> category : values.values()) {
            for (ConfigValue<?> value : category.values()) {
                @SuppressWarnings("unchecked")
                SimpleConfigValue simpleValue = (SimpleConfigValue) value;
                simpleValue.set(simpleValue.getDefault());
            }
        }
    }
}
