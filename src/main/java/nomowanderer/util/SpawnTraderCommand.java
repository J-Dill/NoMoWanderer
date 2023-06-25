package nomowanderer.util;

import com.mojang.brigadier.CommandDispatcher;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.Random;

public class SpawnTraderCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("tryspawntrader");
        LiteralArgumentBuilder<CommandSourceStack> permission =
                builder.requires((stack) -> stack.hasPermission(Commands.LEVEL_ADMINS));
        LiteralArgumentBuilder<CommandSourceStack> pos = permission.then(Commands.argument("pos", Vec3Argument.vec3()).executes(SpawnTraderCommand::trySpawnTrader));
        dispatcher.register(pos);
        return builder;
    }

    private static int trySpawnTrader(CommandContext<CommandSourceStack> stack) {
        WanderingTrader wanderingtrader = EntityType.WANDERING_TRADER.spawn(
                stack.getSource().getLevel(),
                new BlockPos(Vec3Argument.getVec3(stack, "pos")),
                MobSpawnType.EVENT
        );
        if (wanderingtrader != null) {
            for(int j = 0; j < 2; ++j) {
                tryToSpawnLlamaFor(stack.getSource().getLevel(), wanderingtrader, 4);
            }

            wanderingtrader.setDespawnDelay(48000);
        }
        try {
            ServerPlayer serverPlayer = stack.getSource().getPlayerOrException();
            String message = wanderingtrader != null ? "Spawned Trader at " + wanderingtrader.position() : "Trader spawn blocked.";
            serverPlayer.sendSystemMessage(Component.literal(message));
        } catch (CommandSyntaxException e) {
            return 0;
        }
        return 0;
    }

    private static void tryToSpawnLlamaFor(ServerLevel serverLevel, WanderingTrader trader, int range) {
        BlockPos blockpos = findSpawnPositionNear(serverLevel, trader.blockPosition(), range);
        if (blockpos != null) {
            TraderLlama traderllama = EntityType.TRADER_LLAMA.spawn(serverLevel, blockpos, MobSpawnType.EVENT);
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
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, levelReader, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }

}
