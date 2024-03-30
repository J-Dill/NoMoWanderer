package nomowanderer.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegistryObject<T> extends Supplier<T> {

  ResourceLocation getId();

  @Override
  T get();

}