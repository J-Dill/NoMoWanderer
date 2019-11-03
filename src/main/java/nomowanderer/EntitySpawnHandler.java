package nomowanderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    @SubscribeEvent
    public static void onCheckSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
        Entity entity = event.getEntity();
        if (entity instanceof WanderingTraderEntity) {
            BlockPos eventPos = event.getEntity().getPosition();
            IWorld world = event.getWorld();
            // Currently blocking spawn within 64 blocks around the event. Is not efficient...at all.
            Stream<BlockPos> blocks = BlockPos.getAllInBox(eventPos.add(-64.0f, -64.0f, -64.0f), eventPos.add(64.0f, 64.0f, 64.0f));
            boolean foundTileEntity = blocks.anyMatch(blk -> world.getTileEntity(blk) instanceof NoSolicitingSignTileEntity);
            if (foundTileEntity) {
                event.setCanceled(event.isCancelable());
                event.setResult(Event.Result.DENY);
            }
        }
    }

}