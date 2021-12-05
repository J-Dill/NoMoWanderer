package nomowanderer;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nomowanderer.blocks.NoSolicitingSignStand;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.items.NoMoWandererTotemItem;
import nomowanderer.items.NoSolicitingSignItem;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.NoSolicitingSignBlockEntityRenderer;
//import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, NoMoWanderer.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
        ForgeRegistries.BLOCKS, NoMoWanderer.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NoMoWanderer.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_STAND = BLOCKS.register(NoSolicitingSignStand.ID, NoSolicitingSignStand::new);
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_WALL = BLOCKS.register(NoSolicitingSignWall.ID, NoSolicitingSignWall::new);

    //===============
    // Tile Entities
    //===============
    public static final RegistryObject<BlockEntityType<NoSolicitingSignBlockEntity>> NO_SOLICITING_SIGN_TE = BLOCK_ENTITIES.register(
        NoSolicitingSignBlockEntity.ID, () -> BlockEntityType.Builder.of(
            NoSolicitingSignBlockEntity::new, NO_SOLICITING_SIGN_STAND.get(), NO_SOLICITING_SIGN_WALL.get()
        ).build(null)
    );

    //===============
    // Items
    //===============
    public static final RegistryObject<Item> NO_SOLICITING_SIGN_ITEM = ITEMS.register(NoSolicitingSignItem.ID, NoSolicitingSignItem::new);
    public static final RegistryObject<Item> NO_MO_WANDERER_TOTEM_ITEM = ITEMS.register(NoMoWandererTotemItem.ID, NoMoWandererTotemItem::new);

//    @SubscribeEvent
//    public static void onRegisterTEType(RegistryEvent.Register<BlockEntityType<?>> event) {
//        event.getRegistry().register(
//                BlockEntityType.Builder
//                        .create(NoSolicitingSignBlockEntity::new, noSolicitingSignStand, noSolicitingSignWall)
//                        .build(null)
//                        .setRegistryName(NoSolicitingSignBlockEntity.location)
//        );
//    }

//    @SubscribeEvent
//    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
//        event.getRegistry().register(NO_SOLICITING_SIGN_STAND);
//        event.getRegistry().register(NO_SOLICITING_SIGN_WALL);
//    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registry.NO_SOLICITING_SIGN_TE.get(), NoSolicitingSignBlockEntityRenderer::new);
    }

//    @SubscribeEvent
//    public static void registerAsCurio(InterModEnqueueEvent event) {
//        if(ExternalMods.CURIOS.isLoaded()) {
//            InterModComms.sendTo(
//                    "curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build()
//            );
//        }
//    }
}
