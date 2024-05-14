package nomowanderer.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import nomowanderer.CommonRegistry;
import nomowanderer.Config;
import nomowanderer.util.HoverTextUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class NoSolicitingSignItem extends StandingAndWallBlockItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(CommonRegistry.NO_SOLICITING_SIGN_STAND.get(), CommonRegistry.NO_SOLICITING_SIGN_WALL.get(),
            new Properties().stacksTo(16), Direction.DOWN);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> toolTips, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips, Config.SIGN_WATCH_RADIUS);
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, context, toolTips, flag);
    }

}
