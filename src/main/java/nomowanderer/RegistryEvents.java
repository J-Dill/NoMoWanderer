package nomowanderer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import nomowanderer.blocks.NoSolicitingSignStand;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.items.NoSolicitingSignItem;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignTileEntity.ID)
    public static TileEntityType<NoSolicitingSignTileEntity> NO_SOLICITING_SIGN_TE;

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignStand.ID)
    public static Block noSolicitingSignStand = new NoSolicitingSignStand();

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignWall.ID)
    public static Block noSolicitingSignWall = new NoSolicitingSignWall();

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignItem.ID)
    private static NoSolicitingSignItem noSolicitingSignItem = new NoSolicitingSignItem();

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(noSolicitingSignItem);
    }

    @SubscribeEvent
    public static void onRegisterTEType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                TileEntityType.Builder
                        .create(NoSolicitingSignTileEntity::new, noSolicitingSignStand, noSolicitingSignWall)
                        .build(null)
                        .setRegistryName(NoSolicitingSignTileEntity.location)
        );
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(noSolicitingSignStand);
        event.getRegistry().register(noSolicitingSignWall);
    }
}
