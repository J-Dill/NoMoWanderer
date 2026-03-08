package nomowanderer;

import nomowanderer.config.ConfigBuilder;
import nomowanderer.config.ConfigSpec;
import nomowanderer.config.ConfigValue;
import nomowanderer.config.impl.SimpleConfigBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class Config {

    private static final ConfigBuilder SERVER_BUILDER = new SimpleConfigBuilder("nomowanderer", ConfigSpec.ConfigType.SERVER);
    public static final ConfigSpec SERVER_CONFIG;

    public static ConfigValue<Boolean> DISABLE_ENTITY_SPAWNS;
    public static ConfigValue<List<String>> ENTITY_WATCH_LIST;
    public static ConfigValue<Integer> RUG_WATCH_RADIUS;
    public static ConfigValue<Integer> SIGN_WATCH_RADIUS;
    public static ConfigValue<Integer> SPAWN_CAP_WATCH_RADIUS;
    public static ConfigValue<Integer> TALISMAN_WATCH_RADIUS;
    public static ConfigValue<Integer> ENTITY_SPAWN_CAP;

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
            .defineList("entityWatchList",
                Arrays.asList("minecraft:wandering_trader", "minecraft:trader_llama", "rats:plague_doctor", "supplementaries:red_merchant"), it ->
                it instanceof String && (ResourceLocation.tryBySeparator((String) it, ':') != null)
            );
        SERVER_BUILDER.pop();
    }

}
