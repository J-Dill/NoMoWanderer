package nomowanderer.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Simple TOML configuration manager without external dependencies
 */
public class ConfigManager {
    private static final Map<String, ConfigManager> instances = new HashMap<>();
    
    private final String configName;
    private final Path configPath;
    private final ConfigSpec spec;
    private final Map<String, String> loadedValues = new HashMap<>();

    private ConfigManager(String configName, ConfigSpec spec, Path configDir) {
        this.configName = configName;
        this.spec = spec;
        this.configPath = configDir.resolve(configName + "-server.toml");
        
        // Set this manager on all config values
        for (ConfigValue<?> value : spec.getValues().values()) {
            value.setManager(this);
        }
        
        loadConfig();
    }

    public static ConfigManager create(String configName, ConfigSpec spec, Path configDir) {
        String key = configName + ":" + configDir.toString();
        return instances.computeIfAbsent(key, k -> new ConfigManager(configName, spec, configDir));
    }

    private void loadConfig() {
        if (!Files.exists(configPath)) {
            saveConfig();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            parseToml(reader);
            applyLoadedValues();
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            // Use defaults
        }
    }

    private void parseToml(BufferedReader reader) throws IOException {
        String line;
        String currentSection = "";
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            // Handle sections
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                continue;
            }
            
            // Handle key-value pairs
            int equalsIndex = line.indexOf('=');
            if (equalsIndex > 0) {
                String key = line.substring(0, equalsIndex).trim();
                String value = line.substring(equalsIndex + 1).trim();
                
                // Remove quotes if present
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                String fullKey = currentSection.isEmpty() ? key : currentSection + "." + key;
                loadedValues.put(fullKey, value);
            }
        }
    }

    private void applyLoadedValues() {
        for (Map.Entry<String, ConfigValue<?>> entry : spec.getValues().entrySet()) {
            String key = entry.getKey();
            ConfigValue<?> configValue = entry.getValue();
            
            if (loadedValues.containsKey(key)) {
                try {
                    String stringValue = loadedValues.get(key);
                    applyValueFromString(configValue, stringValue);
                } catch (Exception e) {
                    System.err.println("Failed to parse config value for " + key + ": " + e.getMessage());
                    // Keep default value
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void applyValueFromString(ConfigValue<?> configValue, String stringValue) {
        if (configValue instanceof BooleanValue) {
            ((BooleanValue) configValue).set(Boolean.parseBoolean(stringValue));
        } else if (configValue instanceof IntValue) {
            ((IntValue) configValue).set(Integer.parseInt(stringValue));
        } else if (configValue instanceof StringListValue) {
            List<String> parsed = (List<String>) configValue.parseValue(stringValue);
            ((ConfigValue<List<String>>) configValue).set(parsed);
        }
    }

    public void saveConfig() {
        try {
            Files.createDirectories(configPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                writeToml(writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    private void writeToml(BufferedWriter writer) throws IOException {
        Map<String, List<String>> sections = new LinkedHashMap<>();
        
        // Group values by section
        for (Map.Entry<String, ConfigValue<?>> entry : spec.getValues().entrySet()) {
            String key = entry.getKey();
            String section = "";
            String configKey = key;
            
            int lastDot = key.lastIndexOf('.');
            if (lastDot > 0) {
                section = key.substring(0, lastDot);
                configKey = key.substring(lastDot + 1);
            }
            
            sections.computeIfAbsent(section, k -> new ArrayList<>()).add(configKey + "=" + entry.getValue().serializeValue());
        }
        
        // Write sections
        boolean firstSection = true;
        for (Map.Entry<String, List<String>> sectionEntry : sections.entrySet()) {
            String sectionName = sectionEntry.getKey();
            List<String> keyValues = sectionEntry.getValue();
            
            if (!firstSection) {
                writer.newLine();
            }
            firstSection = false;
            
            if (!sectionName.isEmpty()) {
                writer.write("[" + sectionName + "]");
                writer.newLine();
            }
            
            for (String keyValue : keyValues) {
                String key = keyValue.substring(0, keyValue.indexOf('='));
                String fullKey = sectionName.isEmpty() ? key : sectionName + "." + key;
                
                // Write comment if available
                ConfigValue<?> configValue = spec.getValues().get(fullKey);
                if (configValue != null && configValue.getComment() != null) {
                    String[] commentLines = configValue.getComment().split("\n");
                    for (String commentLine : commentLines) {
                        writer.write("# " + commentLine);
                        writer.newLine();
                    }
                }
                
                writer.write(keyValue);
                writer.newLine();
            }
        }
    }

    public void save() {
        saveConfig();
    }
}