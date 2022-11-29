package nomowanderer.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import nomowanderer.Config;

import java.util.List;

public class HoverTextUtil {

    public static void addCommonText(List<Component> toolTips) {
        int chunks = Config.SPAWN_WATCH_RANGE.get();
        toolTips.add(
                Component.literal("Prevents Wandering Trader spawns within ").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(String.valueOf(chunks)).withStyle(ChatFormatting.BLUE))
                        .append(Component.literal(String.format(" chunk%s of the player.", chunks == 1 ? "" : "s")).withStyle(ChatFormatting.GREEN))
        );
        toolTips.add(
                Component.literal("Other entities can be blocked via ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("nomowanderer-common.toml.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
        );
    }

}
