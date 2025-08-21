package nomowanderer.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple configuration manager that reads/writes TOML-like configuration files.
 * This replaces SpectreLib for basic configuration needs.
 */
public class SimpleConfig {
    private final Map<String, Object> values = new LinkedHashMap<>();
    private final Path configPath;
    private final String modId;

    public SimpleConfig(Path configPath, String modId) {
        this.configPath = configPath;
        this.modId = modId;
    }

    public void load() {
        if (!Files.exists(configPath)) {
            return; // Will use defaults
        }

        try (BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            String line;
            String currentSection = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip comments and empty lines
                }

                // Handle sections [section.name]
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }

                // Handle key = value pairs
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    
                    String fullKey = currentSection.isEmpty() ? key : currentSection + "." + key;
                    values.put(fullKey, parseValue(value));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load config file: " + configPath);
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
                writeConfig(writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config file: " + configPath);
            e.printStackTrace();
        }
    }

    private void writeConfig(BufferedWriter writer) throws IOException {
        writer.write("# Configuration file for " + modId);
        writer.newLine();
        writer.newLine();

        Map<String, Map<String, Object>> sections = new LinkedHashMap<>();
        
        // Group values by section
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            int dotIndex = key.lastIndexOf('.');
            String section = dotIndex > 0 ? key.substring(0, dotIndex) : "";
            String localKey = dotIndex > 0 ? key.substring(dotIndex + 1) : key;
            
            sections.computeIfAbsent(section, k -> new LinkedHashMap<>()).put(localKey, value);
        }

        // Write sections
        for (Map.Entry<String, Map<String, Object>> sectionEntry : sections.entrySet()) {
            String sectionName = sectionEntry.getKey();
            Map<String, Object> sectionValues = sectionEntry.getValue();
            
            if (!sectionName.isEmpty()) {
                writer.write("[" + sectionName + "]");
                writer.newLine();
            }
            
            for (Map.Entry<String, Object> valueEntry : sectionValues.entrySet()) {
                String key = valueEntry.getKey();
                Object value = valueEntry.getValue();
                writer.write(key + " = " + formatValue(value));
                writer.newLine();
            }
            writer.newLine();
        }
    }

    private Object parseValue(String value) {
        value = value.trim();
        
        // Remove quotes if present
        if ((value.startsWith("\"") && value.endsWith("\"")) || 
            (value.startsWith("'") && value.endsWith("'"))) {
            value = value.substring(1, value.length() - 1);
        }

        // Try to parse as boolean
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;

        // Try to parse as integer
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {}

        // Try to parse as list [item1, item2, item3]
        if (value.startsWith("[") && value.endsWith("]")) {
            String listContent = value.substring(1, value.length() - 1).trim();
            if (listContent.isEmpty()) {
                return new ArrayList<String>();
            }
            
            List<String> list = new ArrayList<>();
            String[] items = listContent.split(",");
            for (String item : items) {
                item = item.trim();
                if ((item.startsWith("\"") && item.endsWith("\"")) || 
                    (item.startsWith("'") && item.endsWith("'"))) {
                    item = item.substring(1, item.length() - 1);
                }
                list.add(item);
            }
            return list;
        }

        // Return as string
        return value;
    }

    private String formatValue(Object value) {
        if (value instanceof Boolean || value instanceof Integer) {
            return value.toString();
        } else if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) value;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append("\"").append(list.get(i)).append("\"");
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "\"" + value.toString() + "\"";
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = values.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        values.put(key, defaultValue);
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        Object value = values.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        values.put(key, defaultValue);
        return defaultValue;
    }

    public int getInt(String key, int defaultValue, int min, int max) {
        int value = getInt(key, defaultValue);
        if (value < min || value > max) {
            value = defaultValue;
            values.put(key, value);
        }
        return value;
    }

    public String getString(String key, String defaultValue) {
        Object value = values.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        values.put(key, defaultValue);
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key, List<String> defaultValue) {
        Object value = values.get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        values.put(key, defaultValue);
        return defaultValue;
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }
}