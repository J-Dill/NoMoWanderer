package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;


public class NoSolicitingSignWall extends WallSignBlock {

    public static final String ID = "no_soliciting_sign_wall";
    public static final ResourceKey<Block> KEY = ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, ID));

    public NoSolicitingSignWall() {
        super(WoodType.OAK, Properties.of().setId(KEY).mapColor(MapColor.WOOD).noCollission().sound(SoundType.WOOD).strength(1.0F));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NoSolicitingSignBlockEntity(blockPos, blockState);
    }

}