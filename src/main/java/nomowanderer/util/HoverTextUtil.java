package nomowanderer.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class HoverTextUtil {

    public static void addCommonText(List<Component> toolTips, ForgeConfigSpec.IntValue config) {
        int chunks = config.get();
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

    public static void addHoldShiftText(List<Component> toolTips) {
        toolTips.add(
                Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("SHIFT ").withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("for details.").withStyle(ChatFormatting.GRAY))
        );
    }
}
