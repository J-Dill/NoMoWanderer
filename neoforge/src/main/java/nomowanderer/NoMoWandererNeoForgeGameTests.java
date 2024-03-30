package nomowanderer;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import nomowanderer.items.AntiSolicitorTalismanItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = NoMoWandererConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@PrefixGameTestTemplate(false)
public class NoMoWandererNeoForgeGameTests extends NoMoWandererGameTests {

    @SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        event.register(NoMoWandererNeoForgeGameTests.class);
    }

    @BeforeBatch(batch = "nomowanderer.sign")
    public static void beforeSign(ServerLevel level) {
        NoMoWandererGameTests.beforeSign(level);
    }

    @BeforeBatch(batch = "nomowanderer.talisman")
    public static void beforeTalisman(ServerLevel level) {
        NoMoWandererGameTests.beforeTalisman(level);
    }

    @BeforeBatch(batch = "nomowanderer.spawncap")
    public static void beforeSpawnCap(ServerLevel level) {
        NoMoWandererGameTests.beforeSpawnCap(level);
    }

    @BeforeBatch(batch = "nomowanderer.curios")
    public static void beforeCurio(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
        List<ServerPlayer> mockPlayers = level.getPlayers((player) -> player.getGameProfile().getName().equals("test-mock-player"));
        mockPlayers.forEach((player) -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
    }

    @GameTest(batch = "nomowanderer.sign", template = "trader_platform", templateNamespace = "nomowanderer")
    public static void spawnTraderInRange(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderInRange(helper);
    }

    @GameTest(batch = "nomowanderer.sign", template = "trader_platform", templateNamespace = "nomowanderer")
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderOutOfRange(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanIn(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanIn(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanInBarely(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanInBarely(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanInDisabled(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanInDisabled(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOut(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanOut(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOutDisabled(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanOutDisabled(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOutBarely(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderTalismanOutBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapOutBarely(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderCapOutBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapInBarely(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderCapInBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapInBarelyMany(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderCapInBarelyMany(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapTraderAndLlamasLimited(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderCapTraderAndLlamasLimited(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapTraderAndLlamasNoLimit(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderCapTraderAndLlamasNoLimit(helper);
    }

    @GameTest(batch = "nomowanderer.rug", template = "trader_platform_rug", templateNamespace = "nomowanderer")
    public static void spawnTraderByRugOut(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderByRugOut(helper);
    }

    @GameTest(batch = "nomowanderer.rug", template = "trader_platform_rug", templateNamespace = "nomowanderer")
    public static void spawnTraderByRugIn(GameTestHelper helper) {
        NoMoWandererGameTests.spawnTraderByRugIn(helper);
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioIn(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioInBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalisman(helper, true, true);
        trySpawnTraderBarelyIn(helper, getFlipRelativePos(helper, player));
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioInDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN);
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioOut(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioOutDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.curios", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCurioOutBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalisman(helper, true, true);
        BlockPos barelyOut = trySpawnTraderBarelyOut(helper, getFlipRelativePos(helper, player));
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, barelyOut);
    }

    public static Player spawnPlayerWithTalisman(GameTestHelper helper, boolean enabled, boolean inCurioSlot) {
        Player fakePlayer = helper.makeMockSurvivalPlayer();
        helper.getLevel().addFreshEntity(fakePlayer);
        BlockPos playerPos = helper.absolutePos(TALISMAN_PLAYER_SPAWN);
        fakePlayer.absMoveTo(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        AntiSolicitorTalismanItem item = (AntiSolicitorTalismanItem) CommonRegistry.NO_SOLICITING_TALISMAN.get();
        if (inCurioSlot) {
            Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(fakePlayer);
            inventory.ifPresent((inv) -> inv.setEquippedCurio("charm", 0, item.getDefaultInstance(enabled)));
        } else {
            fakePlayer.addItem(item.getDefaultInstance(enabled));
        }
        return fakePlayer;
    }
}
