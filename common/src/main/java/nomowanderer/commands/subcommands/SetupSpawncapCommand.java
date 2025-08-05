package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class SetupSpawncapCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        SubcommandExecutor.updateSpawnWatchRanges();
        SubcommandExecutor.setSpawnCapConfig(1);
        return 1;
    }
}
