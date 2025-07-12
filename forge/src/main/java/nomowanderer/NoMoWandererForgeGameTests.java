package nomowanderer;

import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.gametest.GameTestDontPrefix;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;

/**
 * Forge-specific game tests for NoMoWanderer.
 * For some reason, registering the base class does not work, so we have to
 * redefine the tests here.
 */
@Mod.EventBusSubscriber(modid = NoMoWandererConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@GameTestDontPrefix
public class NoMoWandererForgeGameTests {

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        event.register(NoMoWandererForgeGameTests.class);
    }

    @BeforeBatch(batch = NoMoWandererGameTestsBase.BATCH_SIGN)
    public static void beforeSign(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeSign(level);
    }

    @BeforeBatch(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN)
    public static void beforeTalisman(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeTalisman(level);
    }

    @BeforeBatch(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP)
    public static void beforeSpawnCap(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeSpawnCap(level);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SIGN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM)
    public static void spawnTraderInRange(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderInRange(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SIGN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM)
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderOutOfRange(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanIn(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanIn(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanInBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanInBarely(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanInDisabled(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanInDisabled(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanOut(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOut(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanOutDisabled(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOutDisabled(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_TALISMAN, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderTalismanOutBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOutBarely(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderCapOutBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapOutBarely(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderCapInBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapInBarely(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderCapInBarelyMany(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapInBarelyMany(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderCapTraderAndLlamasLimited(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapTraderAndLlamasLimited(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_SPAWN_CAP, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_TALISMAN)
    public static void spawnTraderCapTraderAndLlamasNoLimit(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapTraderAndLlamasNoLimit(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_RUG, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_RUG)
    public static void spawnTraderByRugOut(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderByRugOut(helper);
    }

    @GameTest(batch = NoMoWandererGameTestsBase.BATCH_RUG, template = NoMoWandererGameTestsBase.TEMPLATE_TRADER_PLATFORM_RUG)
    public static void spawnTraderByRugIn(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderByRugIn(helper);
    }
}
