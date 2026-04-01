package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class SetupTalismanCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        SubcommandExecutor.updateSpawnWatchRanges();
        SubcommandExecutor.setSpawnCapConfig(1);

        ServerLevel level = context.getSource().getLevel();
        List<ServerPlayer> mockPlayers = level.getPlayers((player) -> player.getGameProfile().name().equals("test-mock-player"));
        mockPlayers.forEach((player) -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
        return 1;
    }
}
