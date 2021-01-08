package nomowanderer;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class Config {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue SPAWN_PREV_RANGE;

    public static ForgeConfigSpec.BooleanValue DISABLE_TRADER_SPAWN;

    static {
        // not actually working right now
        SPAWN_PREV_RANGE = COMMON_BUILDER.comment(
                "NOTE: \"Trader\" refers to both the Wandering Trader and the Plague Doctor from the Rats mod.\n\n" +
                "Trader spawn prevention radius (in chunks)")
                .defineInRange("radius", 8, 4, 12);

        DISABLE_TRADER_SPAWN = COMMON_BUILDER.comment("Disable all natural Trader spawns?")
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
