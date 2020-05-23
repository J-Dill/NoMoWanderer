package nomowanderer.items;

import com.lazy.baubles.api.BaubleType;
import com.lazy.baubles.api.IBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import nomowanderer.NoMoWanderer;

public class NoMoWandererTotemItem extends Item implements IBauble {

    public static final String ID = "no_mo_wanderer_totem";

    public NoMoWandererTotemItem() {
        super(new Item.Properties().group(ItemGroup.MISC));
        this.setRegistryName(new ResourceLocation(NoMoWanderer.MODID, ID));
    }

    @Override
    public BaubleType getBaubleType() {
        return BaubleType.TRINKET;
    }
}
