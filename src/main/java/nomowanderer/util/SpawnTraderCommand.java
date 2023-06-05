package nomowanderer.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;

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
        WanderingTrader trader = EntityType.WANDERING_TRADER.spawn(stack.getSource().getLevel(), (ItemStack) null, null,
                new BlockPos(Vec3Argument.getVec3(stack, "pos")), MobSpawnType.EVENT, false, false
        );
        try {
            ServerPlayer serverPlayer = stack.getSource().getPlayerOrException();
            String message = trader != null ? "Spawned Trader at " + trader.position() : "Trader spawn blocked.";
            serverPlayer.sendSystemMessage(Component.literal(message));
        } catch (CommandSyntaxException e) {
            return 0;
        }
        return 0;
    }

}
