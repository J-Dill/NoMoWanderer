package nomowanderer;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class Config {

    private static final SpectreConfigSpec.Builder SERVER_BUILDER = new SpectreConfigSpec.Builder();
    public static final SpectreConfigSpec SERVER_CONFIG;

    public static SpectreConfigSpec.BooleanValue DISABLE_ENTITY_SPAWNS;
    public static SpectreConfigSpec.ConfigValue<List<? extends String>> ENTITY_WATCH_LIST;
    public static SpectreConfigSpec.IntValue RUG_WATCH_RADIUS;
    public static SpectreConfigSpec.IntValue SIGN_WATCH_RADIUS;
    public static SpectreConfigSpec.IntValue SPAWN_CAP_WATCH_RADIUS;
    public static SpectreConfigSpec.IntValue TALISMAN_WATCH_RADIUS;
    public static SpectreConfigSpec.IntValue ENTITY_SPAWN_CAP;

    static {
        generalConfig();
        signConfig();
        spawnCapConfig();
        talismanConfig();
        rugConfig();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void rugConfig() {
        SERVER_BUILDER.push("traderRug");
        RUG_WATCH_RADIUS = SERVER_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <rugRadius> (in chunks)",
                     "around a Trader Rug will instead spawn on the Trader Rug.")
            .defineInRange("rugRadius", 6, 1, 12);
        SERVER_BUILDER.pop();
    }

    private static void talismanConfig() {
        SERVER_BUILDER.push("talisman");
        TALISMAN_WATCH_RADIUS = SERVER_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <talismanRadius> (in chunks)",
                     "around a player with a No Soliciting Talisman will be prevented from spawning.")
            .defineInRange("talismanRadius", 6, 1, 12);
        SERVER_BUILDER.pop();
    }

    private static void spawnCapConfig() {
        SERVER_BUILDER.push("spawnCap");
        SPAWN_CAP_WATCH_RADIUS = SERVER_BUILDER
            .comment("Radius used for the <spawnCap>.")
            .defineInRange("spawnCapRadius", 6, 1, 12);
        ENTITY_SPAWN_CAP = SERVER_BUILDER
            .comment("Maximum amount of each entity in the <entityWatchList> that is allowed within",
                     "<spawnCapRadius> of its own entity type. Set to '0' to disable.")
            .defineInRange("spawnCap", 0, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void signConfig() {
        SERVER_BUILDER.push("noSolicitingSign");
        SIGN_WATCH_RADIUS = SERVER_BUILDER
            .comment("Entities from <entityWatchList> that spawn within <signRadius> (in chunks)",
                     "around a No Soliciting Sign will be prevented from spawning.")
            .defineInRange("signRadius", 6, 1, 12);
        SERVER_BUILDER.pop();
    }

    private static void generalConfig() {
        SERVER_BUILDER.push("general");
        DISABLE_ENTITY_SPAWNS = SERVER_BUILDER.comment("'true' to disable all spawns of entities in <entityWatchList> Ignores <radius>.")
                .define("disableSpawns", false);

        ENTITY_WATCH_LIST = SERVER_BUILDER
            .comment("A list of 'modid:entityName' entries used for the various functions of this mod.")
            .defineList("entityWatchList",
                Arrays.asList("minecraft:wandering_trader", "minecraft:trader_llama", "rats:plague_doctor", "supplementaries:red_merchant"), it ->
                it instanceof String && (ResourceLocation.tryBySeparator((String) it, ':') != null)
            );
        SERVER_BUILDER.pop();
    }

}
