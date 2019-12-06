package nomowanderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    @SubscribeEvent
    public static void maybeBlockTraderSpawn(LivingSpawnEvent.SpecialSpawn event) {
        Entity entity = event.getEntity();
        if (entity instanceof WanderingTraderEntity) {
            BlockPos eventPos = event.getEntity().getPosition();
            IWorld world = event.getWorld();
            IChunk eventChunk = world.getChunk(eventPos);
            ArrayList<IChunk> chunks = getChunksInRadius(world, eventChunk.getPos(), Config.SIGN_SPAWN_PREV_RANGE.get());
            ArrayList<TileEntity> signs = getSignsFromChunks(chunks);
            boolean foundSign = signs.size() > 0; // If we found any signs, stop the Trader spawn.
            if (foundSign) {
                event.setCanceled(event.isCancelable());
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * Search for all NoSolicitingSignTileEntities within the given chunks.
     * @param chunks Chunks to search for signs in.
     * @return List of found signs.
     */
    private static ArrayList<TileEntity> getSignsFromChunks(ArrayList<IChunk> chunks) {
        ArrayList<TileEntity> signs = new ArrayList<>();
        for (IChunk chunk : chunks) {
            if (chunk instanceof Chunk) {
                Chunk newChunk = (Chunk) chunk;
                Map<BlockPos, TileEntity> tileEntities = newChunk.getTileEntityMap();
                for (BlockPos pos : tileEntities.keySet()) {
                    TileEntity te = tileEntities.get(pos);
                    if (te instanceof NoSolicitingSignTileEntity) {
                        signs.add(te);
                    }
                }
            }
        }
        return signs;
    }

    /**
     * Get all chunks within the given radius of the ChunkPos.
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