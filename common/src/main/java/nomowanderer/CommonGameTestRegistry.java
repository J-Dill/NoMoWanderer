package nomowanderer;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import nomowanderer.registry.RegistryObject;
import nomowanderer.registry.RegistryProvider;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class CommonGameTestRegistry {

    public static final RegistryProvider<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            RegistryProvider.get(Registries.TEST_FUNCTION, NoMoWandererConstants.MODID);

    //===============
    // Test Functions
    //===============
    public static final RegistryObject<Consumer<GameTestHelper>> SIGN_SPAWN_TRADER_IN_RANGE = TEST_FUNCTIONS.register(
            "sign_spawn_trader_in_range", () -> CommonGameTests::spawnTraderInRange
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SIGN_SPAWN_TRADER_OUT_OF_RANGE = TEST_FUNCTIONS.register(
            "sign_spawn_trader_out_of_range", () -> CommonGameTests::spawnTraderOutOfRange
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in", () -> CommonGameTests::spawnTraderTalismanIn
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN_BARELY = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in_barely", () -> CommonGameTests::spawnTraderTalismanInBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN_DISABLED = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in_disabled", () -> CommonGameTests::spawnTraderTalismanInDisabled
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out", () -> CommonGameTests::spawnTraderTalismanOut
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT_DISABLED = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out_disabled", () -> CommonGameTests::spawnTraderTalismanOutDisabled
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT_BARELY = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out_barely", () -> CommonGameTests::spawnTraderTalismanOutBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_OUT_BARELY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_out_barely", () -> CommonGameTests::spawnTraderCapOutBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_IN_BARELY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_in_barely", () -> CommonGameTests::spawnTraderCapInBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_IN_BARELY_MANY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_in_barely_many", () -> CommonGameTests::spawnTraderCapInBarelyMany
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_TRADER_AND_LLAMAS_LIMITED = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_trader_and_llamas_limited", () -> CommonGameTests::spawnTraderCapTraderAndLlamasLimited
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_TRADER_AND_LLAMAS_NO_LIMIT = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_trader_and_llamas_no_limit", () -> CommonGameTests::spawnTraderCapTraderAndLlamasNoLimit
    );

    public static final RegistryObject<Consumer<GameTestHelper>> RUG_SPAWN_TRADER_BY_RUG_OUT = TEST_FUNCTIONS.register(
            "rug_spawn_trader_by_rug_out", () -> CommonGameTests::spawnTraderRugOut
    );

    public static final RegistryObject<Consumer<GameTestHelper>> RUG_SPAWN_TRADER_BY_RUG_IN = TEST_FUNCTIONS.register(
            "rug_spawn_trader_by_rug_in", () -> CommonGameTests::spawnTraderRugIn
    );

    public static void init() {}
}
