package nomowanderer.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import nomowanderer.CommonRegistry;
import nomowanderer.Config;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.util.HoverTextUtil;

import java.util.List;
import java.util.function.Consumer;

public class NoSolicitingSignItem extends StandingAndWallBlockItem {

    public static final String ID = "no_soliciting_sign";
    public static final ResourceKey<Item> KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, ID));

    public NoSolicitingSignItem() {
        super(CommonRegistry.NO_SOLICITING_SIGN_STAND.get(), CommonRegistry.NO_SOLICITING_SIGN_WALL.get(),
                Direction.DOWN, new Properties().setId(KEY).stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                TooltipDisplay tooltipDisplay,
                                Consumer<Component> toolTips, TooltipFlag flag) {
        if (Minecraft.getInstance().hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips, Config.SIGN_WATCH_RADIUS);
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, context, tooltipDisplay, toolTips, flag);
    }

}
