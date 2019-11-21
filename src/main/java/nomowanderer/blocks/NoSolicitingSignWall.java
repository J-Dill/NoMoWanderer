package nomowanderer.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import nomowanderer.NoMoWanderer;
import nomowanderer.tileentity.NoSolicitingSignTileEntity;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class NoSolicitingSignWall extends WallSignBlock {

    public static final String ID = "no_soliciting_sign_wall";

    public NoSolicitingSignWall() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(1.0F));
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