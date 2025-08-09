package nomowanderer;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import nomowanderer.items.AntiSolicitorTalismanItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class NeoForgeGameTests extends CommonGameTests {

    public static void spawnTraderCurioIn(GameTestHelper helper) {
        spawnPlayerWithTalismanCurio(helper, true);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    public static void spawnTraderCurioInBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalismanCurio(helper, true);
        trySpawnTraderBarelyIn(helper, getFlipRelativePos(helper, player));
        helper.assertEntityNotPresent(EntityType.WANDERING_TRADER);
        helper.succeed();
    }

    public static void spawnTraderCurioInDisabled(GameTestHelper helper) {
        spawnPlayerWithTalismanCurio(helper, false);
        trySpawnTrader(helper, TALISMAN_PLAYER_SPAWN);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, TALISMAN_PLAYER_SPAWN);
    }

    public static void spawnTraderCurioOut(GameTestHelper helper) {
        spawnPlayerWithTalismanCurio(helper, true);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    public static void spawnTraderCurioOutDisabled(GameTestHelper helper) {
        spawnPlayerWithTalismanCurio(helper, false);
        trySpawnTrader(helper, SIGN_RELATIVE_OUT);
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, SIGN_RELATIVE_OUT);
    }

    public static void spawnTraderCurioOutBarely(GameTestHelper helper) {
        Player player = spawnPlayerWithTalismanCurio(helper, true);
        BlockPos barelyOut = trySpawnTraderBarelyOut(helper, getFlipRelativePos(helper, player));
        helper.succeedWhenEntityPresent(EntityType.WANDERING_TRADER, barelyOut);
    }

    public static Player spawnPlayerWithTalismanCurio(GameTestHelper helper, boolean enabled) {
        // Add a fake player to the level
        Player fakePlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.getLevel().addFreshEntity(fakePlayer);
        BlockPos playerPos = helper.absolutePos(TALISMAN_PLAYER_SPAWN);
        fakePlayer.teleportTo(playerPos.getX(), playerPos.getY(), playerPos.getZ());

        // Add the talisman curio to the fake player's inventory
        AntiSolicitorTalismanItem item = (AntiSolicitorTalismanItem) CommonRegistry.NO_SOLICITING_TALISMAN.get();
        Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(fakePlayer);
        inventory.ifPresent((inv) -> inv.setEquippedCurio("necklace", 0, item.getDefaultInstance(enabled)));

        return fakePlayer;
    }

}
