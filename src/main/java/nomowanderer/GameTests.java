package nomowanderer;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NoMoWanderer.MODID)
public class GameTests {

    private static final BlockPos TRADER_RELATIVE_IN = new BlockPos(1, 2, 46);
    private static final BlockPos TRADER_RELATIVE_OUT = new BlockPos(46, 2, 1);

    @PrefixGameTestTemplate(value = false)
    @GameTest(template = "trader_platform")
    public static void spawnTraderInRange(GameTestHelper helper) {
        trySpawnTrader(helper, TRADER_RELATIVE_IN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(template = "trader_platform")
    public static void spawnTraderOutOfRange(GameTestHelper helper) {
        trySpawnTrader(helper, TRADER_RELATIVE_OUT);
        helper.assertEntityPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    private static void trySpawnTrader(GameTestHelper helper, BlockPos pos) {
        ServerLevel level = helper.getLevel();
        WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(level, null, null, null,
                helper.absolutePos(pos), MobSpawnType.EVENT, true, false
        );
        if (trader != null) {
            trader.removeFreeWill();
        }
    }

}
