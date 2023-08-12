package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;


public class NoSolicitingSignStand extends StandingSignBlock implements EntityBlock {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignStand() {
        super(Properties.of().mapColor(MapColor.WOOD).noCollission().sound(SoundType.WOOD).strength(1.0F), WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NoSolicitingSignBlockEntity(blockPos, blockState);
    }
}