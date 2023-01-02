package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Config;
import nomowanderer.Registry;
import nomowanderer.util.HoverTextUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class TraderRugItem extends BlockItem {

    public TraderRugItem() {
        super(Registry.TRADER_RUG_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> toolTips, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            toolTips.add(
                    Component.literal("Directs Wandering Traders to spawn on this block within ").withStyle(ChatFormatting.GREEN)
                            .append(Component.literal(Config.RUG_WATCH_RADIUS.get().toString()).withStyle(ChatFormatting.BLUE))
                            .append(Component.literal(String.format(" chunk%s of the rug.", (Config.RUG_WATCH_RADIUS.get() == 1 ? "" : "s"))).withStyle(ChatFormatting.GREEN))
            );
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
