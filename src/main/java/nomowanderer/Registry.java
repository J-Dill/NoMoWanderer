package nomowanderer;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nomowanderer.blocks.NoSolicitingSignStand;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.blocks.TraderRugBlock;
import nomowanderer.compat.ExternalMods;
import nomowanderer.items.NoMoWandererTotemItem;
import nomowanderer.items.NoSolicitingSignItem;
import nomowanderer.items.TraderClothPieceItem;
import nomowanderer.items.TraderRugItem;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.NoSolicitingSignBlockEntityRenderer;
import nomowanderer.tileentity.TraderRugBlockEntity;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(modid = NoMoWanderer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NoMoWanderer.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
        ForgeRegistries.BLOCKS, NoMoWanderer.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NoMoWanderer.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_STAND = BLOCKS.register(NoSolicitingSignStand.ID, NoSolicitingSignStand::new);
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_WALL = BLOCKS.register(NoSolicitingSignWall.ID, NoSolicitingSignWall::new);
    public static final RegistryObject<Block> TRADER_RUG_BLOCK = BLOCKS.register(TraderRugBlock.ID, TraderRugBlock::new);

    //===============
    // Tile Entities
    //===============
    public static final RegistryObject<BlockEntityType<NoSolicitingSignBlockEntity>> NO_SOLICITING_SIGN_BE = BLOCK_ENTITIES.register(
        NoSolicitingSignBlockEntity.ID, () -> BlockEntityType.Builder.of(
            NoSolicitingSignBlockEntity::new, NO_SOLICITING_SIGN_STAND.get(), NO_SOLICITING_SIGN_WALL.get()
        ).build(null)
    );
    public static final RegistryObject<BlockEntityType<TraderRugBlockEntity>> TRADER_RUG_BE = BLOCK_ENTITIES.register(
            TraderRugBlockEntity.ID, () -> BlockEntityType.Builder.of(
                TraderRugBlockEntity::new, TRADER_RUG_BLOCK.get()
            ).build(null)
    );

    //===============
    // Items
    //===============
    public static final RegistryObject<Item> NO_SOLICITING_SIGN_ITEM = ITEMS.register(NoSolicitingSignItem.ID, NoSolicitingSignItem::new);
    public static final RegistryObject<Item> NO_SOLICITING_TALISMAN = ITEMS.register(NoMoWandererTotemItem.ID, NoMoWandererTotemItem::new);
    public static final RegistryObject<Item> TRADER_RUG_ITEM = ITEMS.register(TraderRugBlock.ID, TraderRugItem::new);
    public static final RegistryObject<Item> TRADER_CLOTH_PIECE_ITEM = ITEMS.register(TraderClothPieceItem.ID, TraderClothPieceItem::new);

    //===============
    // Other
    //===============
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registry.NO_SOLICITING_SIGN_BE.get(), NoSolicitingSignBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerAsCurio(InterModEnqueueEvent event) {
        if(ExternalMods.CURIOS.isLoaded()) {
            InterModComms.sendTo(
                    "curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build()
            );
        }
    }
}
