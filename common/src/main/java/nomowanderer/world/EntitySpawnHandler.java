package nomowanderer.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import nomowanderer.Config;
import nomowanderer.exception.UnloadedChunkException;
import nomowanderer.items.AntiSolicitorTalismanItem;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.TraderRugBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class EntitySpawnHandler {

    public static SpawnHandlerResult maybeChangeEntitySpawn(Entity entity, ServerLevel level, Optional<Predicate<Player>> invCheck) {
        try {
            return checkSpawn(entity, level, invCheck);
        } catch (UnloadedChunkException ignored) {
            // Event is taking place in unloaded chunk, so do nothing to prevent deadlock.
        }
        return SpawnHandlerResult.UNCHANGED;
    }

    private static boolean isWatchedEntity(Entity entity) {
        List<String> watchedEntities = Config.ENTITY_WATCH_LIST;
        String registryName = getRegistryName(entity);
        return watchedEntities.contains(registryName);
    }

    @NotNull
    private static String getRegistryName(Entity entity) {
        return Objects.requireNonNull(EntityType.getKey(entity.getType())).toString();
    }

    /**
     * If the entity from the spawn event is one of our watched entities, block its spawn if there
     * is a NoSolicitingSign in range or move its spawn if there is a TraderRug in range.
     */
    private static SpawnHandlerResult checkSpawn(Entity entity, ServerLevel level, Optional<Predicate<Player>> invCheck) {
        if (isWatchedEntity(entity)) {
            if (Config.DISABLE_ENTITY_SPAWNS || canFindCancelEntity(entity, level, invCheck)) {
                return SpawnHandlerResult.CANCELLED;
            }
            return checkBlockEntities(entity, level);
        }
        return SpawnHandlerResult.UNCHANGED;
    }

    /**
     * Searches for a player with a totem in their inventory, or other modded slots.
     *
     * @return true if totem is found, false otherwise.
     */
    private static boolean canFindCancelEntity(Entity entity, ServerLevel level, Optional<Predicate<Player>> invCheck) {
        int spawnCapCheckDist = getCheckDist(Config.SPAWN_CAP_WATCH_RADIUS);
        int talismanCheckDist = getCheckDist(Config.TALISMAN_WATCH_RADIUS);

        AABB spawnCapAABB = getAABB(entity, level, spawnCapCheckDist);
        AABB talismanAABB = getAABB(entity, level, talismanCheckDist);
        AABB largestAABB = getLargestAABB(spawnCapAABB, talismanAABB);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, largestAABB);
        HashMap<String, Integer> entityCount = new HashMap<>();
        for(Entity ent : entities) {
            if (ent instanceof Player player) {
                if (!talismanAABB.contains(player.position())) {
                    continue;
                }
                for (ItemStack stack : player.getInventory().items) {
                    if (AntiSolicitorTalismanItem.isEnabled(stack)) {
                        return true;
                    }
                }
                if (invCheck.isPresent()) {
                    if (invCheck.get().test(player)) {
                        return true;
                    }
                }
            } else if (isWatchedEntity(ent) && spawnCapAABB.contains(ent.position())) {
                String registryName = getRegistryName(ent);
                int count = entityCount.get(registryName) != null ? entityCount.get(registryName) : 0;
                entityCount.put(registryName, count + 1);
                Integer spawnCap = Config.ENTITY_SPAWN_CAP;
                if (spawnCap != 0 && registryName.equals(getRegistryName(entity)) && entityCount.get(registryName) >= spawnCap && !entity.getUUID().equals(ent.getUUID())) {
                     return true;
                }
            }
        }
        return false;
    }

    private static AABB getLargestAABB(AABB... areas) {
        AABB largest = null;
        for (AABB aabb : areas) {
            largest = (largest == null || aabb.getXsize() > largest.getXsize() ? aabb : largest);
        }
        return largest;
    }

    private static AABB getAABB(Entity entity, ServerLevel level, int spawnCheckDist) {
        ChunkAccess eventChunk = getChunk(level, entity.blockPosition());
        ChunkPos pos = eventChunk.getPos();
        return new AABB(
                pos.getMaxBlockX() + spawnCheckDist,
                level.getMinBuildHeight(),
                pos.getMinBlockZ() - spawnCheckDist,
                pos.getMinBlockX() - spawnCheckDist,
                level.getMaxBuildHeight(),
                pos.getMaxBlockZ() + spawnCheckDist
        );
    }

    private static int getCheckDist(SpectreConfigSpec.IntValue radius) {
        return (radius.get() * 16) + 1;
    }

    /**
     * Looks for a No Soliciting Sign within the configured distance of the event.
     */
    private static SpawnHandlerResult checkBlockEntities(Entity entity, ServerLevel level) {
        ChunkAccess eventChunk = getChunk(level, entity.getOnPos());
        ArrayList<ChunkAccess> largestChunks = getChunksInRadius(level, eventChunk.getPos(), Math.max(Config.RUG_WATCH_RADIUS, Config.SIGN_WATCH_RADIUS));
        ArrayList<ChunkAccess> rugChunks = getChunksInRadius(level, eventChunk.getPos(), Config.RUG_WATCH_RADIUS);
        ArrayList<ChunkAccess> signChunks = getChunksInRadius(level, eventChunk.getPos(), Config.SIGN_WATCH_RADIUS);
        return lookForBEInChunks(entity, level, largestChunks, rugChunks, signChunks);
    }

    private static SpawnHandlerResult lookForBEInChunks(Entity entity, ServerLevel level, ArrayList<ChunkAccess> largestChunks, ArrayList<ChunkAccess> rugChunks, ArrayList<ChunkAccess> signChunks) {
        for (ChunkAccess chunk : largestChunks) {
            if (chunk instanceof LevelChunk newChunk) {
                Map<BlockPos, BlockEntity> blockEntities = newChunk.getBlockEntities();
                for (BlockPos pos : blockEntities.keySet()) {
                    BlockEntity be = blockEntities.get(pos);
                    BlockPos bePos = be.getBlockPos();
                    if (be instanceof NoSolicitingSignBlockEntity && signChunks.contains(level.getChunk(bePos))) {
                        return SpawnHandlerResult.CANCELLED;
                    } else if (be instanceof TraderRugBlockEntity && rugChunks.contains(level.getChunk(bePos))) {
                        // If spawn isn't cancelled, then look for Trader Rug and spawn in center of it.
                        BlockState blockState = level.getBlockState(bePos.above());
                        boolean validSpawn = !blockState.isSuffocating(level, bePos.above());
                        if (validSpawn) {
                            double x = bePos.getX() + 0.5;
                            double z = bePos.getZ() + 0.5;
                            entity.setPos(x, bePos.getY(), z);
                            return SpawnHandlerResult.MOVED;
                        }
                    }
                }
            }
        }
        return SpawnHandlerResult.UNCHANGED;
    }

    @NotNull
    private static ChunkAccess getChunk(ServerLevel level, BlockPos bePos) {
        int cX = SectionPos.blockToSectionCoord(bePos.getX());
        int cZ = SectionPos.blockToSectionCoord(bePos.getZ());
        Optional<ChunkAccess> chunk = getChunk(level, cX, cZ);
        if (chunk.isEmpty()) {
            throw new UnloadedChunkException();
        }
        return chunk.get();
    }

    @NotNull
    private static Optional<ChunkAccess> getChunk(ServerLevel level, int cX, int cZ) {
        // getChunkNow is used here as regular getChunk calls on Level will all block
        // and generate a new chunk, we can never block here waiting for a new chunk as we will
        // cause a thread deadlock.
        return Optional.ofNullable(level.getChunkSource().getChunkNow(cX, cZ));
    }

    /**
     * Get all chunks within the given radius of the ChunkPos.
     *
     * @param level The level.
     * @param chunkPos The ChunkPos of the event's chunk.
     * @param radius The radius around the ChunkPos to grab chunks from. For example, a radius of 2 would
     *               end up returning 25 chunks.
     * @return ArrayList Array of chunks within given radius surrounding the provided ChunkPos.
     */
    private static ArrayList<ChunkAccess> getChunksInRadius(ServerLevel level, ChunkPos chunkPos, int radius) {
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
                Optional<ChunkAccess> chunk = getChunk(level, curX, curZ);
                chunk.ifPresent(chunks::add);
            }
            curX = startX; // Resetting current X back to start position.
        }
        return chunks;
    }

}