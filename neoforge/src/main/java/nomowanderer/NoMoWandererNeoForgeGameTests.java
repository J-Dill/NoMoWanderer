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
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import nomowanderer.items.AntiSolicitorTalismanItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = NoMoWandererConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
@PrefixGameTestTemplate(false)
public class NoMoWandererNeoForgeGameTests extends NoMoWandererGameTestsBase {

    @SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        event.register(NoMoWandererNeoForgeGameTests.class);
    }

    @BeforeBatch(batch = "nomowanderer.sign")
    public static void beforeSign(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeSign(level);
    }

    @BeforeBatch(batch = "nomowanderer.talisman")
    public static void beforeTalisman(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeTalisman(level);
    }

    @BeforeBatch(batch = "nomowanderer.spawncap")
    public static void beforeSpawnCap(ServerLevel level) {
        NoMoWandererGameTestsBase.beforeSpawnCap(level);
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
        NoMoWandererGameTestsBase.spawnTraderInRange(helper);
    }

    @GameTest(batch = "nomowanderer.sign", template = "trader_platform", templateNamespace = "nomowanderer")
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderOutOfRange(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanIn(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanIn(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanInBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanInBarely(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanInDisabled(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanInDisabled(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOut(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOut(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOutDisabled(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOutDisabled(helper);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderTalismanOutBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderTalismanOutBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapOutBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapOutBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapInBarely(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapInBarely(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapInBarelyMany(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapInBarelyMany(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapTraderAndLlamasLimited(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapTraderAndLlamasLimited(helper);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "trader_platform_talisman", templateNamespace = "nomowanderer")
    public static void spawnTraderCapTraderAndLlamasNoLimit(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderCapTraderAndLlamasNoLimit(helper);
    }

    @GameTest(batch = "nomowanderer.rug", template = "trader_platform_rug", templateNamespace = "nomowanderer")
    public static void spawnTraderByRugOut(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderByRugOut(helper);
    }

    @GameTest(batch = "nomowanderer.rug", template = "trader_platform_rug", templateNamespace = "nomowanderer")
    public static void spawnTraderByRugIn(GameTestHelper helper) {
        NoMoWandererGameTestsBase.spawnTraderByRugIn(helper);
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
        Player fakePlayer = helper.makeMockPlayer(GameType.SURVIVAL);
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
