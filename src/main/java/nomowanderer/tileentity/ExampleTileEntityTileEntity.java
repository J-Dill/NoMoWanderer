package nomowanderer.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;
import nomowanderer.NoMoWanderer;

import static nomowanderer.NoMoWanderer.MODID;

public class ExampleTileEntityTileEntity extends TileEntity implements ITickableTileEntity {
    public static final String ID = "example_tile_entity_tile_entity";
    public static ResourceLocation location = new ResourceLocation(MODID, ID);

    public ExampleTileEntityTileEntity() {
        super(NoMoWanderer.EXAMPLE_TILE_ENTITY);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }

    long lastChangeTime;
    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            if (world != null && !world.isRemote) {
                lastChangeTime = world.getGameTime();
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
            }
        }
    };

    @Override
    public void tick() {
//        if (world.isRemote) {
//            System.out.println("BLA BLAH BLAH BLAH");
//        }
    }
}
