package nomowanderer.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class NoSolicitingSignRenderState extends BlockEntityRenderState {
    public boolean isStanding;
    public WoodType woodType = WoodType.OAK;
    public float rotationDegrees;
}
