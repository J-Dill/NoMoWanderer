package nomowanderer.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Config;
import nomowanderer.Registry;

import javax.annotation.Nullable;
import java.util.List;

public class TraderRugItem extends BlockItem {

    public TraderRugItem() {
        super(Registry.TRADER_RUG_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> toolTips, TooltipFlag flag) {
        String msg = String.format("Directs Wandering Traders to spawn on this block within %d chunks of the rug. " +
                "Other entities can be directed via config.", Config.SPAWN_WATCH_RANGE.get());
        toolTips.add(Component.literal(msg).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
