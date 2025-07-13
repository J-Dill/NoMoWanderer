package nomowanderer.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.registry.services.RegistryUtil;

import java.util.Set;
import java.util.function.BiFunction;

public class FabricRegistryUtil implements RegistryUtil {

  @Override
  public <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
    return FabricBlockEntityTypeBuilder.create(builder::apply, blocks).build();
  }

  @Override
  public <T, D, N> DataComponentType<T> registerDataComponentType(Codec<T> codec, StreamCodec<D, N> nCodec) {
    return DataComponentType.builder().persistent((Codec) codec).networkSynchronized(nCodec).build();
  }

}
