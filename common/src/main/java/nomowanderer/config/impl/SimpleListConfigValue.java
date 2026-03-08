package nomowanderer.config.impl;

import nomowanderer.config.ConfigValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * List configuration value with validation support.
 *
 * @param <T> the type of list elements
 */
public class SimpleListConfigValue<T> extends SimpleConfigValue<List<T>> {
    private final Predicate<Object> validator;

    public SimpleListConfigValue(String name, String comment, List<T> defaultValue, Predicate<Object> validator) {
        super(name, comment, new ArrayList<>(defaultValue));
        this.validator = validator;

        // Validate all default values
        for (Object item : defaultValue) {
            if (!validator.test(item)) {
                throw new IllegalArgumentException(
                    String.format("Default value %s fails validation", item)
                );
            }
        }
    }

    @Override
    public void set(List<T> value) {
        // Validate all values
        if (value != null) {
            for (Object item : value) {
                if (!validator.test(item)) {
                    throw new IllegalArgumentException(
                        String.format("Value %s fails validation", item)
                    );
                }
            }
        }
        super.set(value != null ? new ArrayList<>(value) : null);
    }

    /**
     * Gets the validator for this list.
     *
     * @return the predicate validator
     */
    public Predicate<Object> getValidator() {
        return validator;
    }
}

