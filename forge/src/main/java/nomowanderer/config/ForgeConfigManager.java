package nomowanderer.config;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Forge implementation of ConfigManager.
 * Handles TOML config file generation and persistence for Forge.
 */
public class ForgeConfigManager implements ConfigManager {
    private final Map<String, ConfigSpec> configs = new HashMap<>();
    private final Path configDir;

    public ForgeConfigManager() {
        this.configDir = FMLPaths.CONFIGDIR.get();
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create config directory", e);
            }
        }
    }

    @Override
    public void registerConfig(ConfigSpec spec, String modId) {
        configs.put(modId, spec);
        loadConfig(modId);
    }

    @Override
    public void loadConfig(String modId) {
        ConfigSpec spec = configs.get(modId);
        if (spec == null) {
            throw new IllegalArgumentException("Config not registered for mod: " + modId);
        }

        Path configFile = configDir.resolve(modId + "-" + spec.getType().name().toLowerCase() + ".toml");

        try {
            if (!Files.exists(configFile)) {
                generateConfigFile(configFile, spec);
            } else {
                readConfigFile(configFile, spec);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config for " + modId, e);
        }
    }

    @Override
    public ConfigSpec getConfig(String modId) {
        return configs.get(modId);
    }

    @Override
    public void reloadAll() {
        for (String modId : configs.keySet()) {
            loadConfig(modId);
        }
    }

    private void generateConfigFile(Path path, ConfigSpec spec) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("# Configuration for ").append(spec.getName()).append("\n\n");

        for (Map.Entry<String, Map<String, ConfigValue<?>>> category : spec.getValues().entrySet()) {
            sb.append("[").append(category.getKey()).append("]\n");

            for (ConfigValue<?> value : category.getValue().values()) {
                String comment = value.getComment();
                if (comment != null && !comment.isEmpty()) {
                    for (String line : comment.split("\n")) {
                        sb.append("# ").append(line).append("\n");
                    }
                }

                sb.append(value.getName()).append(" = ");
                Object defaultVal = value.getDefault();

                if (defaultVal instanceof String) {
                    sb.append("\"").append(defaultVal).append("\"");
                } else if (defaultVal instanceof Boolean) {
                    sb.append(defaultVal);
                } else if (defaultVal instanceof Integer) {
                    sb.append(defaultVal);
                } else if (defaultVal instanceof java.util.List) {
                    sb.append("[");
                    java.util.List<?> list = (java.util.List<?>) defaultVal;
                    for (int i = 0; i < list.size(); i++) {
                        Object item = list.get(i);
                        if (item instanceof String) {
                            sb.append("\"").append(item).append("\"");
                        } else {
                            sb.append(item);
                        }
                        if (i < list.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                } else {
                    sb.append(defaultVal);
                }

                sb.append("\n\n");
            }

            sb.append("\n");
        }

        Files.write(path, sb.toString().getBytes());
    }

    private void readConfigFile(Path path, ConfigSpec spec) throws Exception {
        String content = new String(Files.readAllBytes(path));
        String currentCategory = "general";

        for (String line : content.split("\n")) {
            line = line.trim();

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Check for category headers
            if (line.startsWith("[") && line.endsWith("]")) {
                currentCategory = line.substring(1, line.length() - 1);
                continue;
            }

            // Parse key = value
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                Map<String, ConfigValue<?>> categoryValues = spec.getValues().get(currentCategory);
                if (categoryValues != null) {
                    ConfigValue<?> configValue = categoryValues.get(key);
                    if (configValue != null) {
                        try {
                            parseAndSetValue(configValue, value);
                        } catch (Exception e) {
                            // Log but continue on parse errors
                            System.err.println("Failed to parse config value " + key + ": " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseAndSetValue(ConfigValue<?> configValue, String value) {
        Object defaultVal = configValue.getDefault();

        if (defaultVal instanceof Boolean) {
            ((ConfigValue<Boolean>) configValue).set(Boolean.parseBoolean(value));
        } else if (defaultVal instanceof Integer) {
            ((ConfigValue<Integer>) configValue).set(Integer.parseInt(value));
        } else if (defaultVal instanceof String) {
            String strValue = value;
            if (strValue.startsWith("\"") && strValue.endsWith("\"")) {
                strValue = strValue.substring(1, strValue.length() - 1);
            }
            ((ConfigValue<String>) configValue).set(strValue);
        } else if (defaultVal instanceof java.util.List) {
            java.util.List<Object> list = new java.util.ArrayList<>();
            String arrayContent = value;

            if (arrayContent.startsWith("[") && arrayContent.endsWith("]")) {
                arrayContent = arrayContent.substring(1, arrayContent.length() - 1);
            }

            if (!arrayContent.trim().isEmpty()) {
                String[] items = arrayContent.split(",");
                for (String item : items) {
                    item = item.trim();
                    if (item.startsWith("\"") && item.endsWith("\"")) {
                        item = item.substring(1, item.length() - 1);
                    }
                    list.add(item);
                }
            }

            ((ConfigValue<java.util.List<?>>) configValue).set(list);
        }
    }
}

