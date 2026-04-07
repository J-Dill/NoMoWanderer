package nomowanderer.commands;

import nomowanderer.commands.subcommands.SubcommandExecutor;
import nomowanderer.commands.subcommands.SetupSignCommand;
import nomowanderer.commands.subcommands.SetupTalismanCommand;
import nomowanderer.commands.subcommands.SetupSpawncapCommand;
import nomowanderer.commands.subcommands.SetupRugCommand;

public enum NoMoWandererSubcommand {
    SETUP_SIGN("setup_sign", SetupSignCommand.class),
    SETUP_TALISMAN("setup_talisman", SetupTalismanCommand.class),
    SETUP_SPAWNCAP("setup_spawncap", SetupSpawncapCommand.class),
    SETUP_RUG("setup_rug", SetupRugCommand.class);

    private final String name;
    private final Class<? extends SubcommandExecutor> subcommandClass;

    NoMoWandererSubcommand(String name, Class<? extends SubcommandExecutor> subcommandClass) {
        this.name = name;
        this.subcommandClass = subcommandClass;
    }

    public String getName() {
        return name;
    }

    public SubcommandExecutor createExecutor() {
        try {
            return subcommandClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create executor for " + name, e);
        }
    }
}
