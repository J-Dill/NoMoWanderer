package nomowanderer.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Config;
import nomowanderer.Registry;
import nomowanderer.util.HoverTextUtil;
import org.jetbrains.annotations.NotNull;

public class NoSolicitingSignItem extends StandingAndWallBlockItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(Registry.NO_SOLICITING_SIGN_STAND.get(), Registry.NO_SOLICITING_SIGN_WALL.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> toolTips, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            HoverTextUtil.addCommonText(toolTips, Config.SIGN_WATCH_RADIUS);
        } else {
            HoverTextUtil.addHoldShiftText(toolTips);
        }
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
