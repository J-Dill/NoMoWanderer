package nomowanderer;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import nomowanderer.blocks.NoSolicitingSignStand;
import nomowanderer.blocks.NoSolicitingSignWall;
import nomowanderer.blocks.TraderRugBlock;
import nomowanderer.items.AntiSolicitorTalismanItem;
import nomowanderer.items.NoSolicitingSignItem;
import nomowanderer.items.TraderClothPieceItem;
import nomowanderer.items.TraderRugItem;
import nomowanderer.registry.RegistryObject;
import nomowanderer.registry.RegistryProvider;
import nomowanderer.registry.Services;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import nomowanderer.tileentity.TraderRugBlockEntity;

import java.util.function.Consumer;

public class CommonRegistry {

    public static final RegistryProvider<Block> BLOCKS =
            RegistryProvider.get(Registries.BLOCK, NoMoWandererConstants.MODID);
    public static final RegistryProvider<Item> ITEMS =
            RegistryProvider.get(Registries.ITEM, NoMoWandererConstants.MODID);
    public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITIES =
            RegistryProvider.get(Registries.BLOCK_ENTITY_TYPE, NoMoWandererConstants.MODID);
    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENT_TYPES =
            RegistryProvider.get(Registries.DATA_COMPONENT_TYPE, NoMoWandererConstants.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_STAND = BLOCKS.register(NoSolicitingSignStand.ID, NoSolicitingSignStand::new);
    public static final RegistryObject<Block> NO_SOLICITING_SIGN_WALL = BLOCKS.register(NoSolicitingSignWall.ID, NoSolicitingSignWall::new);
    public static final RegistryObject<Block> TRADER_RUG_BLOCK = BLOCKS.register(TraderRugBlock.ID, TraderRugBlock::new);

    //===============
    // Tile Entities
    //===============
    public static final RegistryObject<BlockEntityType<NoSolicitingSignBlockEntity>> NO_SOLICITING_SIGN_BE;
    public static final RegistryObject<BlockEntityType<TraderRugBlockEntity>> TRADER_RUG_BE;

    //===============
    // Items
    //===============
    public static final RegistryObject<Item> NO_SOLICITING_SIGN_ITEM = ITEMS.register(NoSolicitingSignItem.ID, NoSolicitingSignItem::new);
    public static final RegistryObject<Item> NO_SOLICITING_TALISMAN = ITEMS.register(AntiSolicitorTalismanItem.ID, AntiSolicitorTalismanItem::new);
    public static final RegistryObject<Item> TRADER_RUG_ITEM = ITEMS.register(TraderRugBlock.ID, TraderRugItem::new);
    public static final RegistryObject<Item> TRADER_CLOTH_PIECE_ITEM = ITEMS.register(TraderClothPieceItem.ID, TraderClothPieceItem::new);

    public static final RegistryObject<DataComponentType<Boolean>> ENABLED;

    static {
        NO_SOLICITING_SIGN_BE = BLOCK_ENTITIES.register(NoSolicitingSignBlockEntity.ID,
                () -> Services.REGISTRY_UTIL.registerBlockEntityType(NoSolicitingSignBlockEntity::new,
                        NO_SOLICITING_SIGN_STAND.get(), NO_SOLICITING_SIGN_WALL.get()));
        TRADER_RUG_BE = BLOCK_ENTITIES.register(TraderRugBlockEntity.ID,
                () -> Services.REGISTRY_UTIL.registerBlockEntityType(TraderRugBlockEntity::new,
                        TRADER_RUG_BLOCK.get()));

        ENABLED = DATA_COMPONENT_TYPES.register("enabled",
                () -> Services.REGISTRY_UTIL.registerDataComponentType(Codec.BOOL, ByteBufCodecs.BOOL)
        );
    }

    public static void init() {}

}
