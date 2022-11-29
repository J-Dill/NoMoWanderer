package nomowanderer.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import nomowanderer.Config;

import java.util.List;

public class HoverTextUtil {

    public static void addCommonText(List<Component> toolTips) {
        int chunks = Config.SPAWN_WATCH_RANGE.get();
        toolTips.add(
                new TextComponent("Prevents Wandering Trader spawns within ").withStyle(ChatFormatting.GREEN)
                        .append(new TextComponent(String.valueOf(chunks)).withStyle(ChatFormatting.BLUE))
                        .append(new TextComponent(String.format(" chunk%s of the player.", chunks == 1 ? "" : "s")).withStyle(ChatFormatting.GREEN))
        );
        toolTips.add(
                new TextComponent("Other entities can be blocked via ").withStyle(ChatFormatting.GRAY)
                        .append(new TextComponent("nomowanderer-common.toml.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC))
        );
    }

}
