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
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.gametest.GameTestDontPrefix;
import nomowanderer.items.AntiSolicitorTalismanItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

import static nomowanderer.NoMoWandererForgeMod.CURIOS;
import static nomowanderer.NoMoWandererGameTests.*;

@Mod.EventBusSubscriber(modid = NoMoWandererConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@GameTestDontPrefix
public class NoMoWandererForgeGameTests {

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        event.register(NoMoWandererGameTests.class);
        event.register(NoMoWandererForgeGameTests.class);
    }

    @BeforeBatch(batch = "nomowanderer.curios")
    public static void beforeCurio(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
        List<ServerPlayer> mockPlayers = level.getPlayers((player) -> player.getGameProfile().getName().equals("test-mock-player"));
        mockPlayers.forEach((player) -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCurioIn(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCurioInBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalisman(helper, true, true);
        trySpawnTraderBarelyIn(helper, getFlipRelativePos(helper, player));
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCurioInDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN);
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCurioOut(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCurioOutDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.curios", template = "nomowanderer:trader_platform_talisman")
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
        if (CURIOS && inCurioSlot) {
            addToCurioSlot(enabled, fakePlayer, item);
        } else {
            fakePlayer.addItem(item.getDefaultInstance(enabled));
        }
        return fakePlayer;
    }

    private static void addToCurioSlot(boolean enabled, Player fakePlayer, AntiSolicitorTalismanItem item) {
        CuriosApi.getCuriosInventory(fakePlayer).ifPresent((inv) -> inv.setEquippedCurio("charm", 0, item.getDefaultInstance(enabled)));
    }

}
