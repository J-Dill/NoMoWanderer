package nomowanderer.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.Registry;

public class TraderRugBlockEntity extends BlockEntity {

    public static final String ID = "trader_rug_block_entity";

    public TraderRugBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registry.TRADER_RUG_BE.get(), blockPos, blockState);
    }
}
