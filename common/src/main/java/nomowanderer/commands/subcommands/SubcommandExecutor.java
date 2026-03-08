package nomowanderer.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import nomowanderer.Config;
import nomowanderer.config.ConfigValue;

import java.util.Arrays;
import java.util.List;

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

    static void updateSpawnWatchRanges() {
        List<ConfigValue<Integer>> values = Arrays.asList(Config.SIGN_WATCH_RADIUS, Config.SPAWN_CAP_WATCH_RADIUS,
                Config.TALISMAN_WATCH_RADIUS, Config.RUG_WATCH_RADIUS);
        for (ConfigValue<Integer> value : values) {
            if (value.get() > 1) {
                value.set(1);
            }
        }
    }

    static void setSpawnCapConfig(int cap) {
        if (Config.ENTITY_SPAWN_CAP.get() != cap) {
            Config.ENTITY_SPAWN_CAP.set(cap);
        }
    }
}
