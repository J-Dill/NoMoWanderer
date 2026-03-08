package nomowanderer.config.impl;

import nomowanderer.config.ConfigBuilder;

/**
 * Integer configuration value with range constraints.
 */
public class SimpleIntConfigValue extends SimpleConfigValue<Integer> implements ConfigBuilder.IntConfigValue {
    private final int min;
    private final int max;

    public SimpleIntConfigValue(String name, String comment, int defaultValue, int min, int max) {
        super(name, comment, defaultValue);
        this.min = min;
        this.max = max;

        // Validate default is in range
        if (defaultValue < min || defaultValue > max) {
            throw new IllegalArgumentException(
                String.format("Default value %d is outside range [%d, %d]", defaultValue, min, max)
            );
        }
    }

    @Override
    public void set(Integer value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                String.format("Value %d is outside range [%d, %d]", value, min, max)
            );
        }
        super.set(value);
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }
}

