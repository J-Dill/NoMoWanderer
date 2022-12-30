package nomowanderer.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Registry;
import nomowanderer.util.HoverTextUtil;

public class NoSolicitingSignItem extends StandingAndWallBlockItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(Registry.NO_SOLICITING_SIGN_STAND.get(), Registry.NO_SOLICITING_SIGN_WALL.get(),
            new Item.Properties().stacksTo(16), Direction.DOWN);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level,
        List<Component> toolTips, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips);
        } else {
            toolTips.add(
                    Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
                            .append(Component.literal("SHIFT ").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("for details.").withStyle(ChatFormatting.GRAY))
            );
        }
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
