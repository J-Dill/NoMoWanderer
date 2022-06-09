package nomowanderer.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Config;
import nomowanderer.compat.ExternalMods;

public class NoMoWandererTotemItem extends Item {

    public static final String ID = "no_mo_wanderer_totem";

    public NoMoWandererTotemItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level,
        List<Component> toolTips, TooltipFlag flag) {
        int chunks = Config.SPAWN_PREV_RANGE.get();
        String msg = String.format("Prevents Wandering Trader spawns within %d chunks of the player. " +
                "Other entities can be blocked via config.", chunks);
        toolTips.add(Component.literal(msg).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        String totemMessage = String.format(
                "Can be anywhere in your inventory%s.", ExternalMods.CURIOS.isLoaded() ? " or a Curios slot" : ""
        );
        toolTips.add(
                Component.literal(totemMessage)
                        .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        );
        super.appendHoverText(stack, level, toolTips, flag);
    }

}
