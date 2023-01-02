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

    public static ForgeConfigSpec.BooleanValue DISABLE_ENTITY_SPAWNS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_WATCH_LIST;
    public static ForgeConfigSpec.IntValue RUG_WATCH_RADIUS;
    public static ForgeConfigSpec.IntValue SIGN_WATCH_RADIUS;
    public static ForgeConfigSpec.IntValue SPAWN_CAP_WATCH_RADIUS;
    public static ForgeConfigSpec.IntValue TALISMAN_WATCH_RADIUS;
    public static ForgeConfigSpec.IntValue ENTITY_SPAWN_CAP;

    static {
        generalConfig();
        signConfig();
        spawnCapConfig();
        talismanConfig();
        rugConfig();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void rugConfig() {
        COMMON_BUILDER.push("traderRug");
        RUG_WATCH_RADIUS = COMMON_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <rugRadius> (in chunks)",
                     "around a Trader Rug will instead spawn on the Trader Rug.")
            .defineInRange("rugRadius", 6, 1, 12);
        COMMON_BUILDER.pop();
    }

    private static void talismanConfig() {
        COMMON_BUILDER.push("talisman");
        TALISMAN_WATCH_RADIUS = COMMON_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <talismanRadius> (in chunks)",
                     "around a player with a No Soliciting Talisman will be prevented from spawning.")
            .defineInRange("talismanRadius", 6, 1, 12);
        COMMON_BUILDER.pop();
    }

    private static void spawnCapConfig() {
        COMMON_BUILDER.push("spawnCap");
        SPAWN_CAP_WATCH_RADIUS = COMMON_BUILDER
            .comment("Radius used for the <spawnCap>.")
            .defineInRange("spawnCapRadius", 6, 1, 12);
        ENTITY_SPAWN_CAP = COMMON_BUILDER
            .comment("Maximum amount of each entity in the <entityWatchList> that is allowed within",
                     "<spawnCapRadius> of its own entity type. Set to '0' to disable.")
            .defineInRange("spawnCap", 0, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

    private static void signConfig() {
        COMMON_BUILDER.push("noSolicitingSign");
        SIGN_WATCH_RADIUS = COMMON_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <signRadius> (in chunks)",
                     "around a No Soliciting Sign will be prevented from spawning.")
            .defineInRange("signRadius", 6, 1, 12);
        COMMON_BUILDER.pop();
    }

    private static void generalConfig() {
        COMMON_BUILDER.push("general");
        DISABLE_ENTITY_SPAWNS = COMMON_BUILDER.comment("'true' to disable all spawns of entities in <entityWatchList> Ignores <radius>.")
                .define("disableSpawns", false);

        ENTITY_WATCH_LIST = COMMON_BUILDER
            .comment("A list of 'modid:entityName' entries used for the various functions of this mod.")
            .defineList("entityWatchList",
                Arrays.asList("minecraft:wandering_trader", "rats:plague_doctor", "supplementaries:red_merchant"), it ->
                it instanceof String && ResourceLocation.isValidResourceLocation((String) it)
            );
        COMMON_BUILDER.pop();
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
