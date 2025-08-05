package nomowanderer.commands.subcommands;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import nomowanderer.Config;

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
        List<SpectreConfigSpec.IntValue> values = Arrays.asList(Config.SIGN_WATCH_RADIUS, Config.SPAWN_CAP_WATCH_RADIUS,
                Config.TALISMAN_WATCH_RADIUS, Config.RUG_WATCH_RADIUS);
        for (SpectreConfigSpec.IntValue value : values) {
            if (value.get() > 1) {
                value.set(1);
                value.save();
            }
        }
    }

    static void setSpawnCapConfig(int cap) {
        if (Config.ENTITY_SPAWN_CAP.get() != cap) {
            Config.ENTITY_SPAWN_CAP.set(cap);
            Config.ENTITY_SPAWN_CAP.save();
        }
    }
}
