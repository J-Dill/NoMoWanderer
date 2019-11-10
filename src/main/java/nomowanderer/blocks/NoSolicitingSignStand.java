package nomowanderer.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import nomowanderer.NoMoWanderer;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class NoSolicitingSignStand extends StandingSignBlock {

    public static final String ID = "no_soliciting_sign";

    public NoSolicitingSignStand() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f, 10.0f));
        this.setRegistryName(new ResourceLocation(NoMoWanderer.MODID, ID));
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return new NoSolicitingSignTileEntity();
    }

}