package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;


public class NoSolicitingSignStand extends StandingSignBlock implements EntityBlock {

    public static final String ID = "no_soliciting_sign";
    public static final ResourceKey<Block> KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, ID));

    public NoSolicitingSignStand() {
        super(WoodType.OAK, Properties.of().setId(KEY).mapColor(MapColor.WOOD).noCollision().sound(SoundType.WOOD).strength(1.0F));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NoSolicitingSignBlockEntity(blockPos, blockState);
    }
}