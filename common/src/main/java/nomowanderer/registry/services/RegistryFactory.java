package nomowanderer.registry.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import nomowanderer.registry.RegistryProvider;

public interface RegistryFactory {

  <T> RegistryProvider<T> register(ResourceKey<? extends Registry<T>> resourceKey, String modId);

  default <T> RegistryProvider<T> register(Registry<T> registry, String modId) {
    return register(registry.key(), modId);
  }
}
