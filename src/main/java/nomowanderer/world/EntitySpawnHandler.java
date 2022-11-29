package nomowanderer.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.Config;
import nomowanderer.NoMoWanderer;
import nomowanderer.compat.ExternalMods;
import nomowanderer.items.NoMoWandererTotemItem;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.TraderRugBlockEntity;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    @SubscribeEvent
    public static void maybeChangeEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        checkSpawn(event);
    }

    @SubscribeEvent
    public static void maybeChangeEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        checkSpawn(event);
    }

    /**
     * If the entity from the spawn event is one of our watched entities, block its spawn if there
     * is a NoSolicitingSign in range or move its spawn if there is a TraderRug in range.
     */
    private static void checkSpawn(LivingSpawnEvent event) {
        List<? extends String> blockedEntities = Config.ENTITY_WATCH_LIST.get();
        Entity entity = event.getEntity();
        String registryName = Objects.requireNonNull(EntityType.getKey(entity.getType())).toString();
        if (blockedEntities.contains(registryName)) {
            if (Config.DISABLE_ENTITY_SPAWNS.get() || canFindTotem(event)) {
                cancelSpawn(event);
                return;
            }
            List<BlockEntity> foundBEs = getBlockEntities(event);
            for (BlockEntity be : foundBEs) {
                if (be instanceof NoSolicitingSignBlockEntity) {
                    cancelSpawn(event);
                    return;
                } else if (be instanceof TraderRugBlockEntity) {
                    // If spawn isn't cancelled, then look for Trader Rug and spawn in center of it.
                    BlockPos bePos = be.getBlockPos();
                    BlockState blockState = event.getWorld().getBlockState(bePos.above());
                    boolean validSpawn = !blockState.isSuffocating(event.getWorld(), bePos.above());
                    if (validSpawn) {
                        double x = bePos.getX() + 0.5;
                        double z = bePos.getZ() + 0.5;
                        entity.setPos(x, bePos.getY(), z);
                        return;
                    }
                }
            }
        }
    }

    private static void cancelSpawn(LivingSpawnEvent event) {
        if (event.isCancelable()) {
            event.setCanceled(true);
        }
        event.setResult(Event.Result.DENY);
    }

    /**
     * Searches for a player with a totem in their inventory, or other modded slots.
     * Looks in a radius = (16 * SPAWN_PREV_RANGE) around the event for a player.
     *
     * @param event The SpecialSpawn event.
     * @return true if totem is found, false otherwise.
     */
    private static boolean canFindTotem(LivingSpawnEvent event) {
        int spawnCheckDist = Config.SPAWN_WATCH_RANGE.get() * 16;
        boolean curios = ExternalMods.CURIOS.isLoaded();
        AABB aabb = new AABB(
                event.getX() - spawnCheckDist,
                event.getY() - spawnCheckDist,
                event.getZ() - spawnCheckDist,
                event.getX() + spawnCheckDist,
                event.getY() + spawnCheckDist,
                event.getZ() + spawnCheckDist
        );
        List<Player> entities = event.getWorld().getEntitiesOfClass(Player.class, aabb);
        for(Player player : entities) {
            for(ItemStack stack : player.getInventory().items) {
                if (NoMoWandererTotemItem.isEnabled(stack)) {
                    return true;
                }
            }
            if (curios && CuriosApi.getCuriosHelper().findFirstCurio(player, NoMoWandererTotemItem::isEnabled).isPresent()) {
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
    private static List<BlockEntity> getBlockEntities(LivingSpawnEvent event) {
        BlockPos eventPos = event.getEntity().getOnPos();
        LevelAccessor world = event.getWorld();
        ChunkAccess eventChunk = world.getChunk(eventPos);
        ArrayList<ChunkAccess> chunks = getChunksInRadius(world, eventChunk.getPos(), Config.SPAWN_WATCH_RANGE.get());
        return lookForBEInChunks(chunks);
    }

    /**
     * Search for a NoSolicitingSignBlockEntity within the given chunks.
     *
     * @param chunks Chunks to search for signs in.
     * @return True if we found a sign within the chunks, false otherwise.
     */
    private static List<BlockEntity> lookForBEInChunks(ArrayList<ChunkAccess> chunks) {
        List<BlockEntity> matches = new ArrayList<>();
        for (ChunkAccess chunk : chunks) {
            if (chunk instanceof LevelChunk newChunk) {
                Map<BlockPos, BlockEntity> blockEntities = newChunk.getBlockEntities();
                for (BlockPos pos : blockEntities.keySet()) {
                    BlockEntity be = blockEntities.get(pos);
                    if (be instanceof NoSolicitingSignBlockEntity || be instanceof TraderRugBlockEntity) {
                        matches.add(be);
                    }
                }
            }
        }
        return matches;
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