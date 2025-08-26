package nomowanderer.config;

/**
 * Base class for configuration values
 */
public abstract class ConfigValue<T> {
    protected T value;
    protected final T defaultValue;
    protected final String comment;
    private ConfigManager manager;

    protected ConfigValue(T defaultValue, String comment) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.comment = comment;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T getDefault() {
        return defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void save() {
        if (manager != null) {
            manager.save();
        }
    }

    public void setManager(ConfigManager manager) {
        this.manager = manager;
    }

    public abstract boolean isValid(Object value);

    protected abstract T parseValue(String stringValue);

    protected abstract String serializeValue();
}