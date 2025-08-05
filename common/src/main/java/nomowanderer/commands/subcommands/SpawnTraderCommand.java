package nomowanderer.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class SpawnTraderCommand implements SubcommandExecutor {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> registerArguments(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder
                .executes(this::execute) // Allow execution without position (use player position)
                .then(Commands.argument("pos", Vec3Argument.vec3())
                        .executes(this::execute)); // Allow execution with specified position
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        int result = trySpawnTrader(context);
        if (result == 0) {
            return 1; // Command succeeded
        } else {
            context.getSource().sendFailure(Component.literal("Failed to spawn wandering trader"));
            return 0; // Command failed
        }
    }

    private static int trySpawnTrader(CommandContext<CommandSourceStack> context) {
        Vec3 spawnPos;

        // Check if position argument was provided
        try {
            spawnPos = Vec3Argument.getVec3(context, "pos");
        } catch (IllegalArgumentException e) {
            // No position argument, use command source position
            spawnPos = context.getSource().getPosition();
        }

        WanderingTrader wanderingtrader = EntityType.WANDERING_TRADER.spawn(
                context.getSource().getLevel(),
                BlockPos.containing(spawnPos),
                EntitySpawnReason.EVENT
        );

        if (wanderingtrader != null) {
            for(int j = 0; j < 2; ++j) {
                tryToSpawnLlamaFor(context.getSource().getLevel(), wanderingtrader, 4);
            }
            wanderingtrader.setDespawnDelay(48000);
        }

        try {
            ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
            String message = wanderingtrader != null ? "Spawned Trader at " + wanderingtrader.position() : "Trader spawn blocked.";
            serverPlayer.sendSystemMessage(Component.literal(message));
        } catch (CommandSyntaxException e) {
            return 1;
        }
        return 0;
    }

    private static void tryToSpawnLlamaFor(ServerLevel serverLevel, WanderingTrader trader, int range) {
        BlockPos blockpos = findSpawnPositionNear(serverLevel, trader.blockPosition(), range);
        if (blockpos != null) {
            TraderLlama traderllama = EntityType.TRADER_LLAMA.spawn(serverLevel, blockpos, EntitySpawnReason.EVENT);
            if (traderllama != null) {
                traderllama.setLeashedTo(trader, true);
            }
        }
    }

    @Nullable
    private static BlockPos findSpawnPositionNear(LevelReader levelReader, BlockPos blockPos, int range) {
        BlockPos blockpos = null;
        Random random = new Random();

        for(int i = 0; i < 10; ++i) {
            int j = blockPos.getX() + random.nextInt(range * 2) - range;
            int k = blockPos.getZ() + random.nextInt(range * 2) - range;
            int l = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            SpawnPlacementType placementType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);
            if (placementType.isSpawnPositionOk(levelReader, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }
}
