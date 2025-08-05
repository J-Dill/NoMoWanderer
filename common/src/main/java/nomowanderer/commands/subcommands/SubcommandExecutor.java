package nomowanderer.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public interface SubcommandExecutor {
    int execute(CommandContext<CommandSourceStack> context);

    /**
     * Allows subcommands to register their own arguments
     * @param builder The literal command builder for this subcommand
     * @return The builder with any additional arguments added
     */
    default LiteralArgumentBuilder<CommandSourceStack> registerArguments(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.executes(this::execute);
    }
}
