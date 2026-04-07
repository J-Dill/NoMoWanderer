package nomowanderer.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permissions;
import nomowanderer.commands.subcommands.SubcommandExecutor;

public class NoMoWandererBaseCommand {

    public static final String COMMAND_NAME = "nmw";

    public static LiteralArgumentBuilder<CommandSourceStack> create(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(COMMAND_NAME);
        LiteralArgumentBuilder<CommandSourceStack> permission =
                builder.requires((stack) -> stack.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER));

        // Register each subcommand as a literal command with its own arguments
        for (NoMoWandererSubcommand subcommand : NoMoWandererSubcommand.values()) {
            try {
                SubcommandExecutor executor = subcommand.createExecutor();
                LiteralArgumentBuilder<CommandSourceStack> subcommandBuilder = Commands.literal(subcommand.getName());

                // Let the subcommand register its own arguments
                LiteralArgumentBuilder<CommandSourceStack> withArguments = executor.registerArguments(subcommandBuilder);

                permission.then(withArguments);
            } catch (Exception e) {
                // Log error but continue with other subcommands
                System.err.println("Failed to register subcommand " + subcommand.getName() + ": " + e.getMessage());
            }
        }

        dispatcher.register(permission);
        return builder;
    }

}
