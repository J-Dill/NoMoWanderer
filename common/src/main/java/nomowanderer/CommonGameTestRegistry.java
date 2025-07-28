package nomowanderer;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import nomowanderer.registry.RegistryObject;
import nomowanderer.registry.RegistryProvider;

import java.util.function.Consumer;

public class CommonGameTestRegistry {

    public static final RegistryProvider<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            RegistryProvider.get(Registries.TEST_FUNCTION, NoMoWandererConstants.MODID);

    //===============
    // Test Functions
    //===============
    public static final RegistryObject<Consumer<GameTestHelper>> SIGN_SPAWN_TRADER_IN_RANGE = TEST_FUNCTIONS.register(
            "sign_spawn_trader_in_range", () -> NoMoWandererGameTestsBase::spawnTraderInRange
    );

    public static void init() {}
}
