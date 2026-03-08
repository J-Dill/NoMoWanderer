package nomowanderer.config.impl;

import nomowanderer.config.ConfigSpec;
import nomowanderer.config.ConfigValue;
import nomowanderer.config.ConfigBuilder;

import java.util.*;
import java.util.function.Predicate;

/**
 * Basic implementation of ConfigBuilder.
 */
public class SimpleConfigBuilder implements ConfigBuilder {
    private final String specName;
    private final ConfigSpec.ConfigType type;
    private final Map<String, Map<String, ConfigValue<?>>> values = new LinkedHashMap<>();
    private final Deque<String> categoryStack = new ArrayDeque<>();
    private String currentComment = "";

    public SimpleConfigBuilder(String specName, ConfigSpec.ConfigType type) {
        this.specName = specName;
        this.type = type;
    }

    @Override
    public ConfigBuilder push(String category) {
        categoryStack.push(category);
        values.putIfAbsent(getCurrentCategory(), new LinkedHashMap<>());
        return this;
    }

    @Override
    public ConfigBuilder pop() {
        if (!categoryStack.isEmpty()) {
            categoryStack.pop();
        }
        return this;
    }

    @Override
    public ConfigBuilder comment(String... comments) {
        currentComment = String.join("\n", comments);
        return this;
    }

    @Override
    public BooleanConfigValue define(String name, boolean defaultValue) {
        SimpleBooleanConfigValue value = new SimpleBooleanConfigValue(name, currentComment, defaultValue);
        addValue(name, value);
        return value;
    }

    @Override
    public IntConfigValue defineInRange(String name, int defaultValue, int minValue, int maxValue) {
        SimpleIntConfigValue value = new SimpleIntConfigValue(name, currentComment, defaultValue, minValue, maxValue);
        addValue(name, value);
        return value;
    }

    @Override
    public <T> ConfigValue<List<T>> defineList(String name, List<T> defaultValue, Predicate<Object> validator) {
        SimpleListConfigValue<T> value = new SimpleListConfigValue<>(name, currentComment, defaultValue, validator);
        addValue(name, value);
        return value;
    }

    @Override
    public <T> ConfigValue<T> define(String name, T defaultValue) {
        SimpleConfigValue<T> value = new SimpleConfigValue<>(name, currentComment, defaultValue);
        addValue(name, value);
        return value;
    }

    @Override
    public ConfigSpec build() {
        return new SimpleConfigSpec(specName, type, values);
    }

    private String getCurrentCategory() {
        return categoryStack.isEmpty() ? "general" : categoryStack.peek();
    }

    private void addValue(String name, ConfigValue<?> value) {
        String category = getCurrentCategory();
        values.computeIfAbsent(category, k -> new LinkedHashMap<>()).put(name, value);
        currentComment = ""; // Reset comment after use
    }
}

