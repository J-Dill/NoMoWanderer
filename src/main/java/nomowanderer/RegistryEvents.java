package nomowanderer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;
import nomowanderer.blocks.NoSolicitingSignStand;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.items.NoSolicitingSignItem;
import nomowanderer.items.NoMoWandererTotemItem;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;
import nomowanderer.tileentity.NoSolicitingSignTileEntityRenderer;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    //===============
    // Tile Entities
    //===============
    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignTileEntity.ID)
    public static TileEntityType<NoSolicitingSignTileEntity> NO_SOLICITING_SIGN_TE;

    //===============
    // Blocks
    //===============
    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignStand.ID)
    public static Block noSolicitingSignStand = new NoSolicitingSignStand();

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignWall.ID)
    public static Block noSolicitingSignWall = new NoSolicitingSignWall();

    //===============
    // Items
    //===============
    @ObjectHolder(NoMoWanderer.MODID + ":" + NoSolicitingSignItem.ID)
    private static NoSolicitingSignItem noSolicitingSignItem = new NoSolicitingSignItem();

    @ObjectHolder(NoMoWanderer.MODID + ":" + NoMoWandererTotemItem.ID)
    public static NoMoWandererTotemItem noMoWandererTotemItem = new NoMoWandererTotemItem();


    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(noSolicitingSignItem);
        event.getRegistry().register(noMoWandererTotemItem);
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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(NO_SOLICITING_SIGN_TE, NoSolicitingSignTileEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerAsCurio(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
    }
}
