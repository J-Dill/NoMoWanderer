package nomowanderer;

import net.minecraft.gametest.framework.GameTestHelper;
import nomowanderer.registry.RegistryObject;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class NeoForgeGameTestRegistry extends CommonGameTestRegistry {

    //===============
    // Test Functions
    //===============
    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_IN = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_in", () -> NeoForgeGameTests::spawnTraderCurioIn
    );

    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_IN_BARELY = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_in_barely", () -> NeoForgeGameTests::spawnTraderCurioInBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_IN_DISABLED = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_in_disabled", () -> NeoForgeGameTests::spawnTraderCurioInDisabled
    );

    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_OUT = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_out", () -> NeoForgeGameTests::spawnTraderCurioOut
    );

    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_OUT_BARELY = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_out_barely", () -> NeoForgeGameTests::spawnTraderCurioOutBarely
    );

    public static final RegistryObject<Consumer<GameTestHelper>> CURIO_OUT_DISABLED = TEST_FUNCTIONS.register(
            "curio_spawn_trader_curio_out_disabled", () -> NeoForgeGameTests::spawnTraderCurioOutDisabled
    );

    public static void init() {}
}
