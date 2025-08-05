package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SetupRugCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // TODO: Implement rug setup logic
        context.getSource().sendSuccess(() -> Component.literal("Setting up rug..."), false);
        return 1;
    }
}
