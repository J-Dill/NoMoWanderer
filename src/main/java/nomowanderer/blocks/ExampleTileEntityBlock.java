package nomowanderer.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import nomowanderer.tileentity.ExampleTileEntityTileEntity;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class ExampleTileEntityBlock extends Block {

    public ExampleTileEntityBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA).hardnessAndResistance(2.0f, 10.0f));
        this.setRegistryName("example_tile_entity");
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return new ExampleTileEntityTileEntity();
    }

}