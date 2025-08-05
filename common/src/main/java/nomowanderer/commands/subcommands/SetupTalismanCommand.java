package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SetupTalismanCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // TODO: Implement talisman setup logic
        context.getSource().sendSuccess(() -> Component.literal("Setting up talisman..."), false);
        return 1;
    }
}
