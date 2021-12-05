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

    public static ForgeConfigSpec.IntValue SPAWN_PREV_RANGE;
    public static ForgeConfigSpec.BooleanValue DISABLE_ENTITY_SPAWNS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_BLOCK_LIST;

    static {
        ENTITY_BLOCK_LIST = COMMON_BUILDER
            .comment("A list of modid:entity_name entries that will be blocked from spawning.")
            .defineList("entityBlockList",
                Arrays.asList("minecraft:wandering_trader", "rats:plague_doctor"), it ->
                it instanceof String && ResourceLocation.isValidResourceLocation((String) it)
            );

        SPAWN_PREV_RANGE = COMMON_BUILDER.comment("Entity spawn prevention radius (in chunks)")
            .defineInRange("radius", 6, 1, 12);

        DISABLE_ENTITY_SPAWNS = COMMON_BUILDER.comment("Disable all spawns of entities in entityBlockList? Ignores radius.")
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
