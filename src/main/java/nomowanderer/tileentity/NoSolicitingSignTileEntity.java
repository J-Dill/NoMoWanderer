package nomowanderer.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.Registry;

import static nomowanderer.NoMoWanderer.MODID;

public class NoSolicitingSignTileEntity extends BlockEntity {
    public static final String ID = "no_soliciting_sign_tile_entity";
    public static ResourceLocation location = new ResourceLocation(MODID, ID);

    public NoSolicitingSignTileEntity(BlockPos blockPos, BlockState blockState) {
        super(Registry.NO_SOLICITING_SIGN_TE.get(), blockPos, blockState);
    }
}
