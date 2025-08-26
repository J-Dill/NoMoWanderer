package nomowanderer.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Configuration value for string list types with validation
 */
public class StringListValue extends ConfigValue<List<? extends String>> {
    private final Predicate<Object> validator;

    public StringListValue(List<String> defaultValue, Predicate<Object> validator, String comment) {
        super(new ArrayList<>(defaultValue), comment);
        this.validator = validator;
    }

    @Override
    public void set(List<? extends String> value) {
        if (isValid(value)) {
            this.value = new ArrayList<>(value);
        } else {
            throw new IllegalArgumentException("Invalid list value: " + value);
        }
    }

    @Override
    public boolean isValid(Object value) {
        if (!(value instanceof List)) {
            return false;
        }
        List<?> list = (List<?>) value;
        for (Object item : list) {
            if (!validator.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<? extends String> parseValue(String stringValue) {
        List<String> result = new ArrayList<>();
        // Simple parsing - expects format: [item1, item2, item3]
        String cleaned = stringValue.trim();
        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
            if (!cleaned.trim().isEmpty()) {
                String[] items = cleaned.split(",");
                for (String item : items) {
                    String trimmed = item.trim();
                    if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
                        trimmed = trimmed.substring(1, trimmed.length() - 1);
                    }
                    result.add(trimmed);
                }
            }
        }
        
        // Validate the parsed list
        if (isValid(result)) {
            return result;
        } else {
            return new ArrayList<>(defaultValue);
        }
    }

    @Override
    protected String serializeValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < value.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("\"").append(value.get(i)).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }
}