package nomowanderer.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nomowanderer.Config;
import nomowanderer.NoMoWanderer;
import nomowanderer.compat.ExternalMods;
import nomowanderer.exception.UnloadedChunkException;
import nomowanderer.items.AntiSolicitorTalismanItem;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.TraderRugBlockEntity;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID)
public class EntitySpawnHandler {

    public static final boolean CURIOS = ExternalMods.CURIOS.isLoaded();

    @SubscribeEvent
    public static void maybeChangeEntitySpawn(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk()) {
            try {
                checkSpawn(event);
            } catch (UnloadedChunkException ignored) {
                // Event is taking place in unloaded chunk, so do nothing to prevent deadlock.
            }
        }
    }

    private static boolean isWatchedEntity(Entity entity) {
        List<? extends String> watchedEntities = Config.ENTITY_WATCH_LIST.get();
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
    private static void checkSpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (isWatchedEntity(entity)) {
            if (Config.DISABLE_ENTITY_SPAWNS.get() || canFindCancelEntity(event)) {
                cancelSpawn(event);
                return;
            }
            checkBlockEntities(event);
        }
    }

    private static void cancelSpawn(EntityJoinLevelEvent event) {
        if (event.isCancelable()) {
            event.setCanceled(true);
        }
        event.setResult(Event.Result.DENY);
    }

    /**
     * Searches for a player with a totem in their inventory, or other modded slots.
     *
     * @param event The SpecialSpawn event.
     * @return true if totem is found, false otherwise.
     */
    private static boolean canFindCancelEntity(EntityJoinLevelEvent event) {
        int spawnCapCheckDist = getCheckDist(Config.SPAWN_CAP_WATCH_RADIUS);
        int talismanCheckDist = getCheckDist(Config.TALISMAN_WATCH_RADIUS);

        AABB spawnCapAABB = getAABB(event, spawnCapCheckDist);
        AABB talismanAABB = getAABB(event, talismanCheckDist);
        AABB largestAABB = getLargestAABB(spawnCapAABB, talismanAABB);
        List<Entity> entities = event.getLevel().getEntitiesOfClass(Entity.class, largestAABB);
        HashMap<String, Integer> entityCount = new HashMap<>();
        for(Entity entity : entities) {
            if (entity instanceof Player player) {
                if (!talismanAABB.contains(player.position())) {
                    continue;
                }
                for (ItemStack stack : player.getInventory().items) {
                    if (AntiSolicitorTalismanItem.isEnabled(stack)) {
                        return true;
                    }
                }
                if (CURIOS && CuriosApi.getCuriosHelper().findFirstCurio(player, AntiSolicitorTalismanItem::isEnabled).isPresent()) {
                    return true;
                }
            } else if (isWatchedEntity(entity) && spawnCapAABB.contains(entity.position())) {
                String registryName = getRegistryName(entity);
                int count = entityCount.get(registryName) != null ? entityCount.get(registryName) : 0;
                entityCount.put(registryName, count + 1);
                Integer spawnCap = Config.ENTITY_SPAWN_CAP.get();
                if (spawnCap != 0 && registryName.equals(getRegistryName(event.getEntity())) && entityCount.get(registryName) >= spawnCap) {
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

    private static AABB getAABB(EntityJoinLevelEvent event, int spawnCheckDist) {
        ChunkAccess eventChunk = getChunk(event, event.getEntity().blockPosition());
        ChunkPos pos = eventChunk.getPos();
        return new AABB(
                pos.getMaxBlockX() + spawnCheckDist,
                event.getLevel().getMinBuildHeight(),
                pos.getMinBlockZ() - spawnCheckDist,
                pos.getMinBlockX() - spawnCheckDist,
                event.getLevel().getMaxBuildHeight(),
                pos.getMaxBlockZ() + spawnCheckDist
        );
    }

    private static int getCheckDist(ForgeConfigSpec.IntValue radius) {
        return (radius.get() * 16) + 1;
    }

    /**
     * Looks for a No Soliciting Sign within the configured distance of the event.
     *
     * @param event The SpecialSpawn event.
     */
    private static void checkBlockEntities(EntityJoinLevelEvent event) {
        ChunkAccess eventChunk = getChunk(event, event.getEntity().getOnPos());
        ArrayList<ChunkAccess> largestChunks = getChunksInRadius(event, eventChunk.getPos(), Math.max(Config.RUG_WATCH_RADIUS.get(), Config.SIGN_WATCH_RADIUS.get()));
        ArrayList<ChunkAccess> rugChunks = getChunksInRadius(event, eventChunk.getPos(), Config.RUG_WATCH_RADIUS.get());
        ArrayList<ChunkAccess> signChunks = getChunksInRadius(event, eventChunk.getPos(), Config.SIGN_WATCH_RADIUS.get());
        lookForBEInChunks(event, largestChunks, rugChunks, signChunks);
    }

    private static void lookForBEInChunks(EntityJoinLevelEvent event, ArrayList<ChunkAccess> largestChunks, ArrayList<ChunkAccess> rugChunks, ArrayList<ChunkAccess> signChunks) {
        for (ChunkAccess chunk : largestChunks) {
            if (chunk instanceof LevelChunk newChunk) {
                Map<BlockPos, BlockEntity> blockEntities = newChunk.getBlockEntities();
                for (BlockPos pos : blockEntities.keySet()) {
                    BlockEntity be = blockEntities.get(pos);
                    BlockPos bePos = be.getBlockPos();
                    if (be instanceof NoSolicitingSignBlockEntity && signChunks.contains(event.getLevel().getChunk(bePos))) {
                        cancelSpawn(event);
                        return;
                    } else if (be instanceof TraderRugBlockEntity && rugChunks.contains(event.getLevel().getChunk(bePos))) {
                        // If spawn isn't cancelled, then look for Trader Rug and spawn in center of it.
                        BlockState blockState = event.getLevel().getBlockState(bePos.above());
                        boolean validSpawn = !blockState.isSuffocating(event.getLevel(), bePos.above());
                        if (validSpawn) {
                            double x = bePos.getX() + 0.5;
                            double z = bePos.getZ() + 0.5;
                            event.getEntity().setPos(x, bePos.getY(), z);
                            return;
                        }
                    }
                }
            }
        }
    }

    @NotNull
    private static ChunkAccess getChunk(EntityJoinLevelEvent event, BlockPos bePos) {
        int cX = SectionPos.blockToSectionCoord(bePos.getX());
        int cZ = SectionPos.blockToSectionCoord(bePos.getZ());
        Optional<ChunkAccess> chunk = getChunk(event, cX, cZ);
        if (chunk.isEmpty()) {
            throw new UnloadedChunkException();
        }
        return chunk.get();
    }

    @NotNull
    private static Optional<ChunkAccess> getChunk(EntityJoinLevelEvent event, int cX, int cZ) {
        // getChunkNow is used here as regular getChunk calls on Level will all block
        // and generate a new chunk, we can never block here waiting for a new chunk as we will
        // cause a thread deadlock.
        return Optional.ofNullable(event.getLevel().getChunkSource().getChunkNow(cX, cZ));
    }

    /**
     * Get all chunks within the given radius of the ChunkPos.
     *
     * @param event The EntityJoinLevelEvent.
     * @param chunkPos The ChunkPos of the event's chunk.
     * @param radius The radius around the ChunkPos to grab chunks from. For example, a radius of 2 would
     *               end up returning 25 chunks.
     * @return ArrayList Array of chunks within given radius surrounding the provided ChunkPos.
     */
    private static ArrayList<ChunkAccess> getChunksInRadius(EntityJoinLevelEvent event, ChunkPos chunkPos, int radius) {
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
                Optional<ChunkAccess> chunk = getChunk(event, curX, curZ);
                chunk.ifPresent(chunks::add);
            }
            curX = startX; // Resetting current X back to start position.
        }
        return chunks;
    }

}