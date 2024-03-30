package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import nomowanderer.CommonRegistry;
import nomowanderer.Config;
import nomowanderer.util.HoverTextUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class TraderRugItem extends BlockItem {

    public TraderRugItem() {
        super(CommonRegistry.TRADER_RUG_BLOCK.get(), new Properties());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> toolTips, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            toolTips.add(
                    Component.literal("Directs Wandering Traders to spawn on this block within ").withStyle(ChatFormatting.GREEN)
                            .append(Component.literal(Config.RUG_WATCH_RADIUS.get().toString()).withStyle(ChatFormatting.BLUE))
                            .append(Component.literal(" chunks of the rug.").withStyle(ChatFormatting.GREEN))
            );
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
