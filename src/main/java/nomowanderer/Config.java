package nomowanderer;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class Config {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue SPAWN_WATCH_RANGE;
    public static ForgeConfigSpec.BooleanValue DISABLE_ENTITY_SPAWNS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_WATCH_LIST;

    static {
        ENTITY_WATCH_LIST = COMMON_BUILDER
            .comment("A list of 'modid:entity_name' entries.",
                    "These entities will be blocked from spawning if within the radius of a No Soliciting Sign.",
                    "If the entity is not blocked, its spawn will be moved to a Trader Rug if in the radius of one.")
            .defineList("entityWatchList",
                Arrays.asList("minecraft:wandering_trader", "rats:plague_doctor", "supplementaries:red_merchant"), it ->
                it instanceof String && ResourceLocation.isValidResourceLocation((String) it)
            );

        SPAWN_WATCH_RANGE = COMMON_BUILDER.comment("Entity spawn watch radius (in chunks)")
            .defineInRange("radius", 6, 1, 12);

        DISABLE_ENTITY_SPAWNS = COMMON_BUILDER.comment("Disable all spawns of entities in entityWatchList? Ignores radius.")
            .define("disableSpawns", false);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

}
