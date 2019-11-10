package nomowanderer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import nomowanderer.blocks.NoSolicitingSignBlock;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignTileEntity.ID)
    public static TileEntityType<NoSolicitingSignTileEntity> NO_SOLICITING_SIGN_TE;

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignBlock.ID)
    private static Block noSolicitingSignStand = new NoSolicitingSignBlock();

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignWall.ID)
    private static Block noSolicitingSignWall = new NoSolicitingSignWall();

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SignItem((new Item.Properties()).maxStackSize(16).group(ItemGroup.DECORATIONS), noSolicitingSignStand, noSolicitingSignWall).setRegistryName(
                new ResourceLocation(NoMoWanderer.MODID, "no_soliciting_sign")
        ));
    }

    @SubscribeEvent
    public static void onRegisterTEType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                TileEntityType.Builder.create(NoSolicitingSignTileEntity::new, noSolicitingSignStand, noSolicitingSignWall).build(null).setRegistryName(NoSolicitingSignTileEntity.location)
        );
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(noSolicitingSignStand);
        event.getRegistry().register(noSolicitingSignWall);
    }

//    @SubscribeEvent
//    public static void onRegisterContainers(RegistryEvent.Register<ContainerType<?>> event) {
//        event.getRegistry().register(IForgeContainerType.create(((windowId, inv, data) -> new PedestalContainer(windowId, inv, data.readBlockPos()))).setRegistryName(MODID, "pedestal"));
//    }

//    @SubscribeEvent
//    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
//        CraftingHelper.register(BlockConditions.Serializer.INSTANCE);
//    }
}
