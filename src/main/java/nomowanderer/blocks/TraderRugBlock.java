package nomowanderer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nomowanderer.tileentity.TraderRugBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TraderRugBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final String ID = "trader_rug";

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public TraderRugBlock() {
        super(Properties.of().mapColor(MapColor.WOOL).instabreak().noCollission());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TraderRugBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockStateNew,
                                  LevelAccessor accessor, BlockPos blockPos, BlockPos blockPosNew) {
        return !blockState.canSurvive(accessor, blockPos) ?
                    Blocks.AIR.defaultBlockState() :
                    super.updateShape(blockState, direction, blockStateNew, accessor, blockPos, blockPosNew);
    }

    public boolean canSurvive(BlockState blockState, LevelReader reader, BlockPos blockPos) {
        return !reader.isEmptyBlock(blockPos.below());
    }

}
