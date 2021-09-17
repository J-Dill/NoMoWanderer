package nomowanderer;

//import com.lazy.baubles.api.BaublesAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;
//import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    @SubscribeEvent
    public static void maybeBlockEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        checkSpawn(event);
    }

    @SubscribeEvent
    public static void maybeBlockEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        checkSpawn(event);
    }

    private static void checkSpawn(LivingSpawnEvent event) {
        List<? extends String> blockedEntities = Config.ENTITY_BLOCK_LIST.get();
        Entity entity = event.getEntity();
        String registryName = Objects.requireNonNull(entity.getType().getRegistryName()).toString();
        if (blockedEntities.contains(registryName)) {
            boolean cancelSpawn = Config.DISABLE_ENTITY_SPAWNS.get() || canFindTotem(event) || canFindSign(event);
            if (cancelSpawn) {
                // If we found any signs or totems, stop the blocked entity's spawn.
                if (event.isCancelable()) {
                    event.setCanceled(true);
                }
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * Searches for a player with a totem in either a Baubles slot or their inventory.
     * Looks in a 50 block radius square around the event for the player.
     *
     * @param event The SpecialSpawn event.
     * @return true if totem is found, false otherwise.
     */
    private static boolean canFindTotem(LivingSpawnEvent event) {
        final int TRADER_SPAWN_DIST = 50; // It seems MC tries to spawn Traders 48 blocks away, so we're doing 50.
//        boolean baubles = ExternalMods.BAUBLES.isLoaded();
//        boolean curios = ExternalMods.CURIOS.isLoaded();
        AABB aabb = new AABB(
                event.getX() - TRADER_SPAWN_DIST,
                event.getY() - TRADER_SPAWN_DIST,
                event.getZ() - TRADER_SPAWN_DIST,
                event.getX() + TRADER_SPAWN_DIST,
                event.getY() + TRADER_SPAWN_DIST,
                event.getZ() + TRADER_SPAWN_DIST
        );
        Set<Item> totemSet = new HashSet<>();
        totemSet.add(Registry.NO_MO_WANDERER_TOTEM_ITEM.get());
        List<Player> entities = event.getWorld().getEntitiesOfClass(Player.class, aabb);
        for(Player player : entities) {
            if (player.getInventory().hasAnyOf(totemSet)
//                || (curios && CuriosApi.getCuriosHelper().findEquippedCurio(Registry.noMoWandererTotemItem, player).isPresent()) ||
//                    (baubles && -1 != BaublesAPI.isBaubleEquipped(player, Registry.noMoWandererTotemItem))
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks for a No Soliciting Sign within the configured distance of the event.
     *
     * @param event The SpecialSpawn event.
     * @return true if sign is found, false otherwise.
     */
    private static boolean canFindSign(LivingSpawnEvent event) {
        BlockPos eventPos = event.getEntity().getOnPos();
        LevelAccessor world = event.getWorld();
        ChunkAccess eventChunk = world.getChunk(eventPos);
        ArrayList<ChunkAccess> chunks = getChunksInRadius(world, eventChunk.getPos(), Config.SPAWN_PREV_RANGE.get());
        return lookForSignsInChunks(chunks);
    }

    /**
     * Search for a NoSolicitingSignTileEntity within the given chunks.
     *
     * @param chunks Chunks to search for signs in.
     * @return True if we found a sign within the chunks, false otherwise.
     */
    private static boolean lookForSignsInChunks(ArrayList<ChunkAccess> chunks) {
        for (ChunkAccess chunk : chunks) {
            if (chunk instanceof LevelChunk) {
                LevelChunk newChunk = (LevelChunk) chunk;
                Map<BlockPos, BlockEntity> tileEntities = newChunk.getBlockEntities();
                for (BlockPos pos : tileEntities.keySet()) {
                    BlockEntity te = tileEntities.get(pos);
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
    private static ArrayList<ChunkAccess> getChunksInRadius(LevelAccessor w, ChunkPos chunkPos, int radius) {
        /*
            Remember:   North = -Z
                        East  = +X
         */
        int curX = chunkPos.x - radius;
        int curZ = chunkPos.z - radius;
        int startX = curX;
        int endX = chunkPos.x + radius;
        int endZ = chunkPos.z + radius;
        ArrayList<ChunkAccess> chunks = new ArrayList<>();
        for(; curZ <= endZ; curZ++) {
            for(; curX <= endX; curX++) {
                ChunkAccess chunk = w.getChunk(curX, curZ);
                chunks.add(chunk);
            }
            curX = startX; // Resetting current X back to start position.
        }
        return chunks;
    }

}