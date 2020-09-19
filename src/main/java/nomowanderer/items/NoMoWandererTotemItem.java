package nomowanderer.items;

import com.lazy.baubles.api.cap.BaublesCapabilities;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import nomowanderer.NoMoWanderer;
import nomowanderer.compat.ExternalMods;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class NoMoWandererTotemItem extends Item {

    public static final String ID = "no_mo_wanderer_totem";

    public NoMoWandererTotemItem() {
        super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName(new ResourceLocation(NoMoWanderer.MODID, ID));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String msg = "Blocks Wandering Trader spawns around the player when in your inventory or a Baubles slot.";
        tooltip.add(new StringTextComponent(msg).func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    // Doing this instead of directly implementing IBauble so that Baubles can be an optional mod.
    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (ExternalMods.BAUBLES.isLoaded()) {
            com.lazy.baubles.api.IBauble iBauble = () -> com.lazy.baubles.api.BaubleType.TRINKET;
            return new ICapabilityProvider() {
                private final LazyOptional<com.lazy.baubles.api.IBauble> opt = LazyOptional.of(() -> iBauble);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return BaublesCapabilities.ITEM_BAUBLE.orEmpty(cap, opt);
                }
            };
        }
        return null;
    }
}
