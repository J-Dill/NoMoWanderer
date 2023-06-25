package nomowanderer;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import nomowanderer.items.AntiSolicitorTalismanItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@GameTestHolder(NoMoWanderer.MODID)
public class GameTests {

    private static final BlockPos RUG_POS = new BlockPos(0, 2, 47);
    private static final BlockPos SIGN_RELATIVE_IN = new BlockPos(1, 2, 46);
    private static final BlockPos SIGN_RELATIVE_OUT = new BlockPos(46, 2, 1);
    private static final BlockPos TALISMAN_PLAYER_SPAWN = new BlockPos(1, 2, 46);

    @BeforeBatch(batch = "sign")
    public static void beforeSign(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
    }

    @BeforeBatch(batch = "talisman")
    public static void beforeTalisman(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
        List<ServerPlayer> fakePlayers = level.getPlayers((player) -> player instanceof FakePlayer);
        fakePlayers.forEach((player) -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
    }

    @BeforeBatch(batch = "spawnCap")
    public static void beforeSpawnCap(ServerLevel level) {
        updateSpawnWatchRanges();
        setSpawnCapConfig(1);
    }

    private static void updateSpawnWatchRanges() {
        List<ForgeConfigSpec.IntValue> values = Arrays.asList(Config.SIGN_WATCH_RADIUS, Config.SPAWN_CAP_WATCH_RADIUS, Config.TALISMAN_WATCH_RADIUS, Config.RUG_WATCH_RADIUS);
        for (ForgeConfigSpec.IntValue value : values) {
            if (value.get() > 1) {
                value.set(1);
                value.save();
            }
        }
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "sign", template = "trader_platform")
    public static void spawnTraderInRange(GameTestHelper helper) {
        trySpawnTrader(helper, SIGN_RELATIVE_IN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "sign", template = "trader_platform")
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanIn(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanInBarely(GameTestHelper helper) {
        FakePlayer player = spawnPlayerWithTalisman(helper, true);
        trySpawnTraderBarelyIn(helper, player.blockPosition());
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanInDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanOut(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanOutDisabled(GameTestHelper helper) {
        spawnPlayerWithTalisman(helper, false);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "talisman", template = "trader_platform_talisman")
    public static void spawnTraderTalismanOutBarely(GameTestHelper helper) {
        FakePlayer player = spawnPlayerWithTalisman(helper, true);
        BlockPos barelyOut = trySpawnTraderBarelyOut(helper, flipRelativePos(helper.relativePos(player.blockPosition())));
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, barelyOut);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "spawnCap", template = "trader_platform_talisman")
    public static void spawnTraderCapOutBarely(GameTestHelper helper) {
        setSpawnCapConfig(1);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos traderPos = trySpawnTraderBarelyOut(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, traderPos);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "spawnCap", template = "trader_platform_talisman")
    public static void spawnTraderCapInBarely(GameTestHelper helper) {
        setSpawnCapConfig(1);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos barelyIn = trySpawnTraderBarelyIn(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER, barelyIn);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "spawnCap", template = "trader_platform_talisman")
    public static void spawnTraderCapInBarelyMany(GameTestHelper helper) {
        setSpawnCapConfig(12);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        BlockPos barelyIn = trySpawnTraderBarelyInMultiple(helper);
        helper.assertEntitiesPresent(EntityType.WANDERING_TRADER, barelyIn, 11, 2);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "spawnCap", template = "trader_platform_talisman")
    public static void spawnTraderCapTraderAndLlamasLimited(GameTestHelper helper) {
        setSpawnCapConfig(1);
        WanderingTrader trader = trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        tryToSpawnLlamaFor(helper, trader);
        tryToSpawnLlamaFor(helper, trader);
        helper.assertEntitiesPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN, 1, 2);
        helper.assertEntitiesPresent(EntityType.TRADER_LLAMA, TALISMAN_PLAYER_SPAWN, 1, 2);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "spawnCap", template = "trader_platform_talisman")
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

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "rug", template = "trader_platform_rug")
    public static void spawnTraderByRugOut(GameTestHelper helper) {
        BlockPos blockPos = trySpawnTraderBarelyOut(helper, RUG_POS);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, blockPos);
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "rug", template = "trader_platform_rug")
    public static void spawnTraderByRugIn(GameTestHelper helper) {
        BlockPos blockPos = trySpawnTraderBarelyIn(helper, RUG_POS);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER, blockPos);
        helper.succeed();
    }

    /*
    ====================== Test Helpers ======================
     */

    private static BlockPos trySpawnTraderBarelyInMultiple(GameTestHelper helper) {
        BlockPos pos = null;
        for (int i = 0; i <= 100; i++) {
            BlockPos barelyIn = trySpawnTraderBarelyIn(helper, GameTests.TALISMAN_PLAYER_SPAWN);
            if (pos == null) {
                pos = barelyIn;
            }
        }
        return pos;
    }

    private static void setSpawnCapConfig(int cap) {
        if (Config.ENTITY_SPAWN_CAP.get() != cap) {
            Config.ENTITY_SPAWN_CAP.set(cap);
            Config.ENTITY_SPAWN_CAP.save();
        }
    }

    private static WanderingTrader trySpawnTrader(GameTestHelper helper, BlockPos pos) {
        return trySpawnTrader(helper, pos, true);
    }

    private static WanderingTrader trySpawnTrader(GameTestHelper helper, BlockPos pos, boolean absolute) {
        ServerLevel level = helper.getLevel();
        WanderingTrader trader = (WanderingTrader) EntityType.WANDERING_TRADER.spawn(level, null, null, absolute ? helper.absolutePos(pos) : pos, MobSpawnType.EVENT, false, false);
        if (trader != null) {
            trader.removeFreeWill();
        }
        return trader;
    }

    private static BlockPos trySpawnTraderBarelyIn(GameTestHelper helper, BlockPos pos) {
        return trySpawnTraderBarely(helper, pos, true);
    }

    private static BlockPos trySpawnTraderBarelyOut(GameTestHelper helper, BlockPos pos) {
        return trySpawnTraderBarely(helper, pos, false);
    }

    private static BlockPos trySpawnTraderBarely(GameTestHelper helper, BlockPos pos, boolean in) {
        BlockPos blockPos = getBarelyPos(helper, pos, in);
        trySpawnTrader(helper, blockPos, true);
        return blockPos;
    }

    @NotNull
    private static BlockPos getBarelyPos(GameTestHelper helper, BlockPos pos, boolean in) {
        BlockPos absolutePos = helper.absolutePos(pos);
        ChunkPos chunkPos = helper.getLevel().getChunk(absolutePos).getPos();
        int offset = in ? 15 : 20;
        BlockPos relativePos = helper.relativePos(new BlockPos(chunkPos.getMaxBlockX(), absolutePos.getY(), chunkPos.getMinBlockZ()).offset(offset, 0, -offset));
        return flipRelativePos(relativePos);
    }

    @NotNull
    private static BlockPos flipRelativePos(BlockPos relativePos) {
        return new BlockPos(-relativePos.getX(), relativePos.getY(), -relativePos.getZ());
    }

    private static FakePlayer spawnPlayerWithTalisman(GameTestHelper helper, boolean enabled) {
        FakePlayer fakePlayer = FakePlayerFactory.get(helper.getLevel(), new GameProfile(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(10)));
        helper.getLevel().addFreshEntity(fakePlayer);
        BlockPos playerPos = helper.absolutePos(TALISMAN_PLAYER_SPAWN);
        fakePlayer.absMoveTo(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        AntiSolicitorTalismanItem item = (AntiSolicitorTalismanItem) Registry.NO_SOLICITING_TALISMAN.get();
        fakePlayer.addItem(item.getDefaultInstance(enabled));
        return fakePlayer;
    }

    private static void tryToSpawnLlamaFor(GameTestHelper helper, WanderingTrader trader) {
        ServerLevel serverLevel = helper.getLevel();
        TraderLlama traderllama = (TraderLlama) EntityType.TRADER_LLAMA.spawn(serverLevel, null, null, trader.blockPosition(), MobSpawnType.EVENT, false, false);
        assert traderllama != null;
        traderllama.removeFreeWill();
    }

}
