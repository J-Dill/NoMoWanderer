package nomowanderer.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import nomowanderer.NoMoWandererConstants;

public class TraderClothPieceItem extends Item {

    public static final String ID = "trader_cloth_piece";
    public static final ResourceKey<Item> KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, ID));

    public TraderClothPieceItem() {
        super(new Properties().setId(KEY));
    }

}
