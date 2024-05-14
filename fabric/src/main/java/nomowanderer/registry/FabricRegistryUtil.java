package nomowanderer.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
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
    return BlockEntityType.Builder.of(builder::apply, blocks).build(null);
  }

  @Override
  public <T, D, N> DataComponentType<T> registerDataComponentType(Codec<T> codec, StreamCodec<D, N> nCodec) {
    return DataComponentType.builder().persistent((Codec) codec).networkSynchronized(nCodec).build();
  }

}
