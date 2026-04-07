package nomowanderer.registry;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface RegistryObject<T> extends Supplier<T> {

  Identifier getId();

  @Override
  T get();

}