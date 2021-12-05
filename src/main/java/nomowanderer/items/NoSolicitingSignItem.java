package nomowanderer.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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

public class NoSolicitingSignItem extends StandingAndWallBlockItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(Registry.NO_SOLICITING_SIGN_STAND.get(), Registry.NO_SOLICITING_SIGN_WALL.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level,
        List<Component> toolTips, TooltipFlag flag) {
        int chunks = Config.SPAWN_PREV_RANGE.get();
        String msg = String.format("Prevents configured entity spawns within %d chunks of the sign.", chunks);
        toolTips.add(new TextComponent(msg).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
