package nomowanderer.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.Registry;

public class NoSolicitingSignBlockEntity extends BlockEntity {
    public static final String ID = "no_soliciting_sign_tile_entity";

    public NoSolicitingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registry.NO_SOLICITING_SIGN_TE.get(), blockPos, blockState);
    }
}
