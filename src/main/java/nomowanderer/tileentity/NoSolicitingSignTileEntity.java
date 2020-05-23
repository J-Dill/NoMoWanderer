package nomowanderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import nomowanderer.RegistryEvents;

import static nomowanderer.NoMoWanderer.MODID;

public class NoSolicitingSignTileEntity extends TileEntity {
    public static final String ID = "no_soliciting_sign_tile_entity";
    public static ResourceLocation location = new ResourceLocation(MODID, ID);

    public NoSolicitingSignTileEntity() {
        super(RegistryEvents.NO_SOLICITING_SIGN_TE);
    }
}
