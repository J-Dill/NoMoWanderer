package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import nomowanderer.CommonRegistry;
import nomowanderer.Config;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.blocks.TraderRugBlock;
import nomowanderer.util.HoverTextUtil;

import java.util.List;
import java.util.function.Consumer;

public class TraderRugItem extends BlockItem {

    public static final ResourceKey<Item> KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, TraderRugBlock.ID));

    public TraderRugItem() {
        super(CommonRegistry.TRADER_RUG_BLOCK.get(), new Properties().setId(KEY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
                                Consumer<Component> toolTips, TooltipFlag flag) {
        if (Minecraft.getInstance().hasShiftDown()) {
            toolTips.accept(
                    Component.literal("Directs Wandering Traders to spawn on this block within ").withStyle(ChatFormatting.GREEN)
                            .append(Component.literal(Config.RUG_WATCH_RADIUS.get().toString()).withStyle(ChatFormatting.BLUE))
                            .append(Component.literal(" chunks of the rug.").withStyle(ChatFormatting.GREEN))
            );
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, context, tooltipDisplay, toolTips, flag);
    }

}
