package nomowanderer.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.ResourceLocation;
import nomowanderer.NoMoWanderer;
import nomowanderer.RegistryEvents;

public class NoSolicitingSignItem extends WallOrFloorItem {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignItem() {
        super(RegistryEvents.noSolicitingSignStand, RegistryEvents.noSolicitingSignWall, (new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS));
        this.setRegistryName(new ResourceLocation(NoMoWanderer.MODID, ID));
    }
}
