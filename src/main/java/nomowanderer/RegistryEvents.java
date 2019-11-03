package nomowanderer;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import nomowanderer.blocks.NoSolicitingSignBlock;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

import java.util.Arrays;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignTileEntity.ID)
    public static TileEntityType<NoSolicitingSignTileEntity> NO_SOLICITING_SIGN_TE;

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignBlock.ID)
    private static Block noSolicitingSignBlock = new NoSolicitingSignBlock();
    private static Block[] blocks = new Block[]{noSolicitingSignBlock};

    @SubscribeEvent
    public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
        Arrays.stream(blocks).map(block -> new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(block.getRegistryName())).forEach(item -> event.getRegistry().register(item));
    }

    @SubscribeEvent
    public static void onRegisterTEType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                TileEntityType.Builder.create(
                        NoSolicitingSignTileEntity::new, noSolicitingSignBlock).build(null).setRegistryName(NoSolicitingSignTileEntity.location
                )
        );
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blocks);
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
