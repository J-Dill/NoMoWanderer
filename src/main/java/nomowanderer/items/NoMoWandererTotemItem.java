package nomowanderer.items;

//import com.lazy.baubles.api.bauble.BaubleType;
//import com.lazy.baubles.api.bauble.IBauble;
//import com.lazy.baubles.api.cap.BaublesCapabilities;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class NoMoWandererTotemItem extends Item {

    public static final String ID = "no_mo_wanderer_totem";

    public NoMoWandererTotemItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level,
        List<Component> toolTips, TooltipFlag flag) {
        toolTips.add(new TextComponent("Blocks configurable entity spawns around the player."));
        toolTips.add(new TextComponent("Can be in your inventory, a Baubles or Curios slot."));
        super.appendHoverText(stack, level, toolTips, flag);
    }

    // Doing this instead of directly implementing IBauble so that Baubles can be an optional mod.
//    @Nullable
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
//        if (ExternalMods.BAUBLES.isLoaded()) {
//            IBauble iBauble = (stack1) -> BaubleType.TRINKET;
//            return new ICapabilityProvider() {
//                private final LazyOptional<IBauble> opt = LazyOptional.of(() -> iBauble);
//
//                @Nonnull
//                @Override
//                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//                    return BaublesCapabilities.ITEM_BAUBLE.orEmpty(cap, opt);
//                }
//            };
//        }
//        return null;
//    }
}
