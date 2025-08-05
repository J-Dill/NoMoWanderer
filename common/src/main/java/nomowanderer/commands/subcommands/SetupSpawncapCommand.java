package nomowanderer.commands.subcommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SetupSpawncapCommand implements SubcommandExecutor {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // TODO: Implement spawn cap setup logic
        context.getSource().sendSuccess(() -> Component.literal("Setting up spawn cap..."), false);
        return 1;
    }
}
