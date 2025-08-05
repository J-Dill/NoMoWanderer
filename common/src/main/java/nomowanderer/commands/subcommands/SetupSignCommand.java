package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SetupSignCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // TODO: Implement sign setup logic
        context.getSource().sendSuccess(() -> Component.literal("Setting up sign..."), false);
        return 1;
    }
}
