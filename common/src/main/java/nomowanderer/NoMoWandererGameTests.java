package nomowanderer;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import nomowanderer.items.AntiSolicitorTalismanItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NoMoWandererGameTests {

    public static final BlockPos RUG_POS = new BlockPos(0, 2, 47);
    public static final BlockPos SIGN_RELATIVE_IN = new BlockPos(1, 2, 46);
    public static final BlockPos SIGN_RELATIVE_OUT = new BlockPos(46, 2, 1);
    public static final BlockPos TALISMAN_PLAYER_SPAWN = new BlockPos(1, 2, 46);

    @BeforeBatch(batch = "nomowanderer.sign")
    public static void beforeSign(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
    }

    @BeforeBatch(batch = "nomowanderer.talisman")
    public static void beforeTalisman(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
        List<ServerPlayer> mockPlayers = level.getPlayers((player) -> player.getGameProfile().getName().equals("test-mock-player"));
        mockPlayers.forEach((player) -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
    }

    @BeforeBatch(batch = "nomowanderer.spawncap")
    public static void beforeSpawnCap(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
    }

    public static void updateSpawnWatchRanges() {
        List<SpectreConfigSpec.IntValue> values = Arrays.asList(Config.SIGN_WATCH_RADIUS, Config.SPAWN_CAP_WATCH_RADIUS, Config.TALISMAN_WATCH_RADIUS, Config.RUG_WATCH_RADIUS);
        for (SpectreConfigSpec.IntValue value : values) {
            if (value.get() > 1) {
                value.set(1);
                value.save();
            }
        }
    }

    @GameTest(batch = "nomowanderer.sign", template = "nomowanderer:trader_platform")
    public static void spawnTraderInRange(GameTestHelper helper) {
        trySpawnTrader(helper, SIGN_RELATIVE_IN);
        helper.succeedWhenEntityNotPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_IN);
    }

    @GameTest(batch = "nomowanderer.sign", template = "nomowanderer:trader_platform")
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanIn(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanInBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalisman(helper, true);
        trySpawnTraderBarelyIn(helper, getFlipRelativePos(helper, player));
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanInDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanOut(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanOutDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @GameTest(batch = "nomowanderer.talisman", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderTalismanOutBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalisman(helper, true);
        BlockPos barelyOut = trySpawnTraderBarelyOut(helper, getFlipRelativePos(helper, player));
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, barelyOut);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCapOutBarely(GameTestHelper helper) {
        setSpawnCapConfig(1);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos traderPos = trySpawnTraderBarelyOut(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, traderPos);
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCapInBarely(GameTestHelper helper) {
        setSpawnCapConfig(1);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos barelyIn = trySpawnTraderBarelyIn(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER, barelyIn);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCapInBarelyMany(GameTestHelper helper) {
        setSpawnCapConfig(12);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos barelyIn = trySpawnTraderBarelyInMultiple(helper, TALISMAN_PLAYER_SPAWN, 100);
        helper.assertEntitiesPresent(EntityType.WANDERING_TRADER, barelyIn, 11, 2);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCapTraderAndLlamasLimited(GameTestHelper helper) {
        setSpawnCapConfig(1);
        WanderingTrader trader = trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        tryToSpawnLlamaFor(helper, trader);
        tryToSpawnLlamaFor(helper, trader);
        helper.assertEntitiesPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN, 1, 2);
        helper.assertEntitiesPresent(EntityType.TRADER_LLAMA, TALISMAN_PLAYER_SPAWN, 1, 2);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.spawncap", template = "nomowanderer:trader_platform_talisman")
    public static void spawnTraderCapTraderAndLlamasNoLimit(GameTestHelper helper) {
        setSpawnCapConfig(0);
        WanderingTrader trader = trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        tryToSpawnLlamaFor(helper, trader);
        tryToSpawnLlamaFor(helper, trader);
        tryToSpawnLlamaFor(helper, trader);
        helper.assertEntitiesPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN, 1, 2);
        helper.assertEntitiesPresent(EntityType.TRADER_LLAMA, TALISMAN_PLAYER_SPAWN, 3, 0);
        helper.succeed();
    }

    @GameTest(batch = "nomowanderer.rug", template = "nomowanderer:trader_platform_rug")
    public static void spawnTraderByRugOut(GameTestHelper helper) {
        BlockPos blockPos = trySpawnTraderBarelyOut(helper, RUG_POS);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, blockPos);
    }

    @GameTest(batch = "nomowanderer.rug", template = "nomowanderer:trader_platform_rug")
    public static void spawnTraderByRugIn(GameTestHelper helper) {
        BlockPos blockPos = trySpawnTraderBarelyIn(helper, RUG_POS);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER, blockPos);
        helper.succeed();
    }

    /*
    ====================== Test Helpers ======================
     */

    public static BlockPos trySpawnTraderBarelyInMultiple(GameTestHelper helper, BlockPos blockPos, int num) {
        BlockPos pos = null;
        for (int i = 0; i <= num; i++) {
            BlockPos barelyIn = trySpawnTraderBarelyIn(helper, blockPos);
            if (pos == null) {
                pos = barelyIn;
            }
        }
        return pos;
    }

    public static void setSpawnCapConfig(int cap) {
        if (Config.ENTITY_SPAWN_CAP.get() != cap) {
            Config.ENTITY_SPAWN_CAP.set(cap);
            Config.ENTITY_SPAWN_CAP.save();
        }
    }

    public static WanderingTrader trySpawnTrader(GameTestHelper helper, BlockPos pos) {
        return trySpawnTrader(helper, pos, true);
    }

    public static WanderingTrader trySpawnTrader(GameTestHelper helper, BlockPos pos, boolean absolute) {
        ServerLevel level = helper.getLevel();
        WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(level, (ItemStack) null, null,
                absolute ? helper.absolutePos(pos) : pos, MobSpawnType.EVENT, true, false
        );
        if (trader != null) {
            trader.removeFreeWill();
        }
        return trader;
    }

    public static BlockPos trySpawnTraderBarelyIn(GameTestHelper helper, BlockPos pos) {
        return trySpawnTraderBarely(helper, pos, true);
    }

    public static BlockPos trySpawnTraderBarelyOut(GameTestHelper helper, BlockPos pos) {
        return trySpawnTraderBarely(helper, pos, false);
    }

    public static BlockPos trySpawnTraderBarely(GameTestHelper helper, BlockPos pos, boolean in) {
        BlockPos blockPos = getBarelyPos(helper, pos, in);
        trySpawnTrader(helper, blockPos, true);
        return blockPos;
    }

    @NotNull
    public static BlockPos getBarelyPos(GameTestHelper helper, BlockPos pos, boolean in) {
        BlockPos absolutePos = helper.absolutePos(pos);
        ChunkPos chunkPos = helper.getLevel().getChunk(absolutePos).getPos();
        int offset = in ? 15 : 20;
        BlockPos relativePos = helper.relativePos(new BlockPos(chunkPos.getMaxBlockX(), absolutePos.getY(), chunkPos.getMinBlockZ()).offset(offset, 0, -offset));
        return flipRelativePos(relativePos);
    }

    @NotNull
    public static BlockPos flipRelativePos(BlockPos relativePos) {
        return new BlockPos(-relativePos.getX(), relativePos.getY(), -relativePos.getZ());
    }

    public static Player spawnPlayerWithTalisman(GameTestHelper helper, boolean enabled) {
        Player fakePlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.getLevel().addFreshEntity(fakePlayer);
        BlockPos playerPos = helper.absolutePos(TALISMAN_PLAYER_SPAWN);
        fakePlayer.absMoveTo(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        AntiSolicitorTalismanItem item = (AntiSolicitorTalismanItem) CommonRegistry.NO_SOLICITING_TALISMAN.get();
        fakePlayer.addItem(item.getDefaultInstance(enabled));
        return fakePlayer;
    }

    public static void tryToSpawnLlamaFor(GameTestHelper helper, WanderingTrader trader) {
        ServerLevel serverLevel = helper.getLevel();
        TraderLlama traderllama = EntityType.TRADER_LLAMA.spawn(serverLevel, trader.blockPosition(), MobSpawnType.EVENT);
        assert traderllama != null;
        traderllama.removeFreeWill();
    }

    public static @NotNull BlockPos getFlipRelativePos(GameTestHelper helper, Player player) {
        return flipRelativePos(helper.relativePos(player.blockPosition()));
    }

}
