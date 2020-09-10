package nomowanderer;

//import com.lazy.baubles.api.BaublesApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.compat.ExternalMods;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;
//import top.theillusivec4.curios.api.CuriosAPI;

import java.util.*;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    @SubscribeEvent
    public static void maybeBlockTraderSpawn(LivingSpawnEvent.SpecialSpawn event) {
        Entity entity = event.getEntity();
        if (entity instanceof WanderingTraderEntity) {
            boolean cancelSpawn = canFindTotem(event) || canFindSign(event);
            if (cancelSpawn) {
                // If we found any signs or totems, stop the Wandering Trader spawn.
                event.setCanceled(event.isCancelable());
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * Searches for a player with a totem in either a Baubles slot or their inventory.
     * Looks in a 50 block radius square around the event for the player.
     *
     * @param event The Wandering Trader SpecialSpawn event.
     * @return true if totem is found, false otherwise.
     */
    private static boolean canFindTotem(LivingSpawnEvent.SpecialSpawn event) {
        final int TRADER_SPAWN_DIST = 50; // It seems MC tries to spawn Traders 48 blocks away, so we're doing 50.
        boolean baubles = ExternalMods.BAUBLES.isLoaded();
        boolean curios = ExternalMods.CURIOS.isLoaded();
        AxisAlignedBB aabb = new AxisAlignedBB(
                event.getX() - TRADER_SPAWN_DIST,
                event.getY() - TRADER_SPAWN_DIST,
                event.getZ() - TRADER_SPAWN_DIST,
                event.getX() + TRADER_SPAWN_DIST,
                event.getY() + TRADER_SPAWN_DIST,
                event.getZ() + TRADER_SPAWN_DIST
        );
        Set<Item> totemSet = new HashSet<>();
        totemSet.add(RegistryEvents.noMoWandererTotemItem);
        List<PlayerEntity> entities = event.getWorld().getEntitiesWithinAABB(PlayerEntity.class, aabb);
        for(PlayerEntity player : entities) {
            if (player.inventory.hasAny(totemSet)
//                    ||
//                    (baubles && -1 != BaublesApi.isBaubleEquipped(player, RegistryEvents.noMoWandererTotemItem)) ||
//                    (curios && CuriosAPI.getCurioEquipped(RegistryEvents.noMoWandererTotemItem, player).isPresent())
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks for a No Soliciting Sign within the configured distance of the event.
     *
     * @param event The Wandering Trader SpecialSpawn event.
     * @return true if sign is found, false otherwise.
     */
    private static boolean canFindSign(LivingSpawnEvent.SpecialSpawn event) {
        BlockPos eventPos = event.getEntity().func_233580_cy_(); // Get position of Wandering Trader.
        IWorld world = event.getWorld();
        IChunk eventChunk = world.getChunk(eventPos);
        ArrayList<IChunk> chunks = getChunksInRadius(world, eventChunk.getPos(), Config.SPAWN_PREV_RANGE.get());
        return lookForSignsInChunks(chunks);
    }

    /**
     * Search for a NoSolicitingSignTileEntity within the given chunks.
     *
     * @param chunks Chunks to search for signs in.
     * @return True if we found a sign within the chunks, false otherwise.
     */
    private static boolean lookForSignsInChunks(ArrayList<IChunk> chunks) {
        for (IChunk chunk : chunks) {
            if (chunk instanceof Chunk) {
                Chunk newChunk = (Chunk) chunk;
                Map<BlockPos, TileEntity> tileEntities = newChunk.getTileEntityMap();
                for (BlockPos pos : tileEntities.keySet()) {
                    TileEntity te = tileEntities.get(pos);
                    if (te instanceof NoSolicitingSignTileEntity) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Get all chunks within the given radius of the ChunkPos.
     *
     * @param w The chunk's world.
     * @param chunkPos The ChunkPos of the event's chunk.
     * @param radius The radius around the ChunkPos to grab chunks from. For example, a radius of 2 would
     *               end up returning 25 chunks.
     * @return Array of chunks within given radius surrounding the provided ChunkPos.
     */
    private static ArrayList<IChunk> getChunksInRadius(IWorld w, ChunkPos chunkPos, int radius) {
        /*
            Remember:   North = -Z
                        East  = +X
         */
        int curX = chunkPos.x - radius;
        int curZ = chunkPos.z - radius;
        int startX = curX;
        int endX = chunkPos.x + radius;
        int endZ = chunkPos.z + radius;
        ArrayList<IChunk> chunks = new ArrayList<>();
        for(; curZ <= endZ; curZ++) {
            for(; curX <= endX; curX++) {
                IChunk chunk = w.getChunk(curX, curZ);
                chunks.add(chunk);
            }
            curX = startX; // Resetting current X back to start position.
        }
        return chunks;
    }

}