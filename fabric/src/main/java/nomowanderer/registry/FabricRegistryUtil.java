package nomowanderer.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.registry.services.RegistryUtil;

import java.util.function.BiFunction;

public class FabricRegistryUtil implements RegistryUtil {

  @Override
  public <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
    return FabricBlockEntityTypeBuilder.create(builder::apply, blocks).build(null);
  }

}
