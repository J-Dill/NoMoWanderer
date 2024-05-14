package nomowanderer.registry.services;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface RegistryUtil {

  <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks);

  <T, D, N> DataComponentType<T> registerDataComponentType(Codec<T> codec, StreamCodec<D, N> nCodec);
}
