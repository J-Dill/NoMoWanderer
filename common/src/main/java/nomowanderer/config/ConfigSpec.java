package nomowanderer.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConfigSpec {
    private final Map<String, ConfigValue<?>> values = new LinkedHashMap<>();
    private final Map<String, String> comments = new LinkedHashMap<>();
    private final List<String> categoryStack = new ArrayList<>();

    public static class Builder {
        private final ConfigSpec spec = new ConfigSpec();

        public Builder push(String category) {
            spec.categoryStack.add(category);
            return this;
        }

        public Builder pop() {
            if (!spec.categoryStack.isEmpty()) {
                spec.categoryStack.remove(spec.categoryStack.size() - 1);
            }
            return this;
        }

        public Builder comment(String comment) {
            return comment(comment, null);
        }

        public Builder comment(String comment, String additionalComment) {
            String fullComment = additionalComment != null ? comment + "\n" + additionalComment : comment;
            String currentPath = getCurrentPath();
            spec.comments.put(currentPath + "._next_comment", fullComment);
            return this;
        }

        public BooleanValue define(String key, boolean defaultValue) {
            String path = getCurrentPath() + (getCurrentPath().isEmpty() ? "" : ".") + key;
            String commentKey = getCurrentPath() + "._next_comment";
            String comment = spec.comments.remove(commentKey);
            
            BooleanValue value = new BooleanValue(defaultValue, comment);
            spec.values.put(path, value);
            return value;
        }

        public IntValue defineInRange(String key, int defaultValue, int min, int max) {
            String path = getCurrentPath() + (getCurrentPath().isEmpty() ? "" : ".") + key;
            String commentKey = getCurrentPath() + "._next_comment";
            String comment = spec.comments.remove(commentKey);
            
            IntValue value = new IntValue(defaultValue, min, max, comment);
            spec.values.put(path, value);
            return value;
        }

        public <T> ConfigValue<List<? extends String>> defineList(String key, List<? extends String> defaultValue, Predicate<Object> validator) {
            String path = getCurrentPath() + (getCurrentPath().isEmpty() ? "" : ".") + key;
            String commentKey = getCurrentPath() + "._next_comment";
            String comment = spec.comments.remove(commentKey);
            
            @SuppressWarnings("unchecked")
            ConfigValue<List<? extends String>> value = (ConfigValue<List<? extends String>>) new StringListValue(new ArrayList<>(defaultValue), validator, comment);
            spec.values.put(path, value);
            return value;
        }

        private String getCurrentPath() {
            return String.join(".", spec.categoryStack);
        }

        public ConfigSpec build() {
            return spec;
        }
    }

    public Map<String, ConfigValue<?>> getValues() {
        return values;
    }

    public Map<String, String> getComments() {
        return comments;
    }
}