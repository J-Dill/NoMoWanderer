package nomowanderer;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NoMoWanderer.MODID)
public class GameTests {

    private static final BlockPos SIGN_RELATIVE_IN = new BlockPos(1, 2, 46);
    private static final BlockPos SIGN_RELATIVE_OUT = new BlockPos(46, 2, 1);
    private static final BlockPos TRADER_SPAWN_POS = new BlockPos(0, 1, 0);
    private static final BlockPos RUG_POS = new BlockPos(2, 2, 2);

    @BeforeBatch(batch = "sign")
    public static void before(ServerLevel level) {
        if (Config.SPAWN_WATCH_RANGE.get() > 1) {
            Config.SPAWN_WATCH_RANGE.set(1);
            Config.SPAWN_WATCH_RANGE.save();
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
        helper.assertEntityPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(batch = "rug", template = "trader_rug")
    public static void spawnTraderByRug(GameTestHelper helper) {
        trySpawnTrader(helper, TRADER_SPAWN_POS);
        helper.assertEntityPresent(EntityType.WANDERING_TRADER, RUG_POS);
        helper.succeed();
    }

    private static void trySpawnTrader(GameTestHelper helper, BlockPos pos) {
        ServerLevel level = helper.getLevel();
        WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(level, (ItemStack) null, null,
                helper.absolutePos(pos), MobSpawnType.EVENT, true, false
        );
        if (trader != null) {
            trader.removeFreeWill();
        }
    }

}
