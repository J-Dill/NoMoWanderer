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

    //================
    // Setup Functions
    //================


    //===============
    // Test Functions
    //===============
    public static final RegistryObject<Consumer<GameTestHelper>> SIGN_SPAWN_TRADER_IN_RANGE = TEST_FUNCTIONS.register(
            "sign_spawn_trader_in_range", () -> NoMoWandererGameTestsBase::spawnTraderInRange
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SIGN_SPAWN_TRADER_OUT_OF_RANGE = TEST_FUNCTIONS.register(
            "sign_spawn_trader_out_of_range", () -> NoMoWandererGameTestsBase::spawnTraderOutOfRange
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in", () -> NoMoWandererGameTestsBase::spawnTraderTalismanIn
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN_BARELY = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in_barely", () -> NoMoWandererGameTestsBase::spawnTraderTalismanInBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_IN_DISABLED = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_in_disabled", () -> NoMoWandererGameTestsBase::spawnTraderTalismanInDisabled
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out", () -> NoMoWandererGameTestsBase::spawnTraderTalismanOut
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT_DISABLED = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out_disabled", () -> NoMoWandererGameTestsBase::spawnTraderTalismanOutDisabled
    );

    public static final RegistryObject<Consumer<GameTestHelper>> TALISMAN_SPAWN_TRADER_TALISMAN_OUT_BARELY = TEST_FUNCTIONS.register(
            "talisman_spawn_trader_talisman_out_barely", () -> NoMoWandererGameTestsBase::spawnTraderTalismanOutBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_OUT_BARELY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_out_barely", () -> NoMoWandererGameTestsBase::spawnTraderCapOutBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_IN_BARELY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_in_barely", () -> NoMoWandererGameTestsBase::spawnTraderCapInBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_IN_BARELY_MANY = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_in_barely_many", () -> NoMoWandererGameTestsBase::spawnTraderCapInBarelyMany
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_TRADER_AND_LLAMAS_LIMITED = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_trader_and_llamas_limited", () -> NoMoWandererGameTestsBase::spawnTraderCapTraderAndLlamasLimited
    );

    public static final RegistryObject<Consumer<GameTestHelper>> SPAWNCAP_SPAWN_TRADER_CAP_TRADER_AND_LLAMAS_NO_LIMIT = TEST_FUNCTIONS.register(
            "spawncap_spawn_trader_cap_trader_and_llamas_no_limit", () -> NoMoWandererGameTestsBase::spawnTraderCapTraderAndLlamasNoLimit
    );

    public static final RegistryObject<Consumer<GameTestHelper>> RUG_SPAWN_TRADER_BY_RUG_OUT = TEST_FUNCTIONS.register(
            "rug_spawn_trader_by_rug_out", () -> NoMoWandererGameTestsBase::spawnTraderRugOut
    );

    public static final RegistryObject<Consumer<GameTestHelper>> RUG_SPAWN_TRADER_BY_RUG_IN = TEST_FUNCTIONS.register(
            "rug_spawn_trader_by_rug_in", () -> NoMoWandererGameTestsBase::spawnTraderRugIn
    );

    public static void init() {}
}
