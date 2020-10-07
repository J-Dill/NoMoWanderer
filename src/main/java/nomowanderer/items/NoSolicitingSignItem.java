package nomowanderer.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nomowanderer.Config;
import nomowanderer.NoMoWanderer;
import nomowanderer.RegistryEvents;

import javax.annotation.Nullable;
import java.util.List;

public class NoSolicitingSignItem extends WallOrFloorItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(RegistryEvents.noSolicitingSignStand, RegistryEvents.noSolicitingSignWall, (new Item.Properties()).maxStackSize(16).group(ItemGroup.MISC));
        this.setRegistryName(new ResourceLocation(NoMoWanderer.MODID, ID));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int chunks = Config.SPAWN_PREV_RANGE.get();
        String msg = String.format("Prevents Wandering Trader spawns within %d chunk%s of the sign.",
                chunks,
                chunks == 1 ? "" : "s"
        );
        tooltip.add(new StringTextComponent(msg).mergeStyle(TextFormatting.GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
