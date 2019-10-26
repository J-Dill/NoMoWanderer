package nomowanderer;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nomowanderer.blocks.ExampleTileEntityBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Arrays;

@Mod(NoMoWanderer.MODID)
public class NoMoWanderer {

    public static final String MODID = "nomowanderer";

    private static final Logger LOGGER = LogManager.getLogger(NoMoWanderer.MODID + " Mod Event Subscriber");

    private static final Block EXAMPLE_BLOCK = new ExampleTileEntityBlock();
    private static Block[] blocks = new Block[]{EXAMPLE_BLOCK};

    public NoMoWanderer() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        modEventBus.addListener(this::onRegisterBlocks);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
            Arrays.stream(blocks).map(block -> new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(block.getRegistryName())).forEach(item -> event.getRegistry().register(item));
        }

//        @SubscribeEvent
//        public static void onRegisterContainers(RegistryEvent.Register<ContainerType<?>> event) {
//            event.getRegistry().register(IForgeContainerType.create(((windowId, inv, data) -> new PedestalContainer(windowId, inv, data.readBlockPos()))).setRegistryName(MODID, "pedestal"));
//        }

//        @SubscribeEvent
//        public static void onRegisterTEType(RegistryEvent.Register<TileEntityType<?>> event) {
//            event.getRegistry().register(TileEntityType.Builder.create(Example::new, PEDESTAL_BLOCK).build(null).setRegistryName(new ResourceLocation(MODID, "pedestal")));
//        }

        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(blocks);
        }

//        @SubscribeEvent
//        public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
//            CraftingHelper.register(BlockConditions.Serializer.INSTANCE);
//        }
    }
}