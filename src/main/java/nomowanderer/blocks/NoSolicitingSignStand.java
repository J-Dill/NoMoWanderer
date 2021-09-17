package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;


public class NoSolicitingSignStand extends StandingSignBlock implements EntityBlock {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignStand() {
        super(Properties.of(Material.WOOD).noCollission().sound(SoundType.WOOD), WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NoSolicitingSignTileEntity(blockPos, blockState);
    }
}