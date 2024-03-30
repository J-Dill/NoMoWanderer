package nomowanderer.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.registry.services.RegistryUtil;

import java.util.function.BiFunction;

public class NeoForgeRegistryUtil implements RegistryUtil {

  @SuppressWarnings("all")
  @Override
  public <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
    return BlockEntityType.Builder.of(builder::apply, blocks).build(null);
  }

}
