package nomowanderer.config;

/**
 * Configuration value for integer types with range validation
 */
public class IntValue extends ConfigValue<Integer> {
    private final int minValue;
    private final int maxValue;

    public IntValue(int defaultValue, int minValue, int maxValue, String comment) {
        super(defaultValue, comment);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void set(Integer value) {
        if (isValid(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Value " + value + " is not within range [" + minValue + ", " + maxValue + "]");
        }
    }

    @Override
    public boolean isValid(Object value) {
        if (!(value instanceof Integer)) {
            return false;
        }
        int intValue = (Integer) value;
        return intValue >= minValue && intValue <= maxValue;
    }

    @Override
    protected Integer parseValue(String stringValue) {
        int parsed = Integer.parseInt(stringValue.trim());
        if (!isValid(parsed)) {
            return defaultValue;
        }
        return parsed;
    }

    @Override
    protected String serializeValue() {
        return value.toString();
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
}