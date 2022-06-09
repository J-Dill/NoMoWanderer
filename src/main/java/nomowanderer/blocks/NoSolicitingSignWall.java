package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;


public class NoSolicitingSignWall extends WallSignBlock {

    public static final String ID = "no_soliciting_sign_wall";

    public NoSolicitingSignWall() {
        super(Properties.of(Material.WOOD).noCollission().sound(SoundType.WOOD).strength(1.0F), WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NoSolicitingSignBlockEntity(blockPos, blockState);
    }

}