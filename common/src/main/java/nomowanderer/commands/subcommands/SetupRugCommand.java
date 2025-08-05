package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class SetupRugCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        SubcommandExecutor.updateSpawnWatchRanges();
        return 1;
    }
}
