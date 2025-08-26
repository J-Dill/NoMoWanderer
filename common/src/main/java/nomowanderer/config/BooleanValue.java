package nomowanderer.config;

/**
 * Configuration value for boolean types
 */
public class BooleanValue extends ConfigValue<Boolean> {

    public BooleanValue(boolean defaultValue, String comment) {
        super(defaultValue, comment);
    }

    @Override
    public void save() {
        // Implementation will be handled by ConfigManager
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Boolean;
    }

    @Override
    protected Boolean parseValue(String stringValue) {
        return Boolean.parseBoolean(stringValue.trim());
    }

    @Override
    protected String serializeValue() {
        return value.toString();
    }
}