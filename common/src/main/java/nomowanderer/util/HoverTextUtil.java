package nomowanderer.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import nomowanderer.config.IntValue;

import java.util.function.Consumer;

public class HoverTextUtil {

    public static void addCommonText(Consumer<Component> toolTips, IntValue config) {
        int chunks = config.get();
        toolTips.accept(
                Component.literal("Prevents Wandering Trader spawns within ").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(String.valueOf(chunks)).withStyle(ChatFormatting.BLUE))
                        .append(Component.literal(String.format(" chunk%s of the player.", chunks == 1 ? "" : "s")).withStyle(ChatFormatting.GREEN))
        );
        toolTips.accept(
                Component.literal("Other entities can be blocked via ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("nomowanderer-server.toml.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
        );
    }

    public static void addHoldShiftText(Consumer<Component> toolTips) {
        toolTips.accept(
                Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("SHIFT ").withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("for details.").withStyle(ChatFormatting.GRAY))
        );
    }
}
