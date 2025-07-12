package nomowanderer.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import nomowanderer.registry.services.RegistryFactory;

import java.util.function.Supplier;

public class FabricRegistryProvider implements RegistryFactory {

  @Override
  public <T> RegistryProvider<T> register(ResourceKey<? extends Registry<T>> resourceKey,
                                          String modId) {
    return new Provider<>(modId, resourceKey);
  }

  @Override
  public <T> RegistryProvider<T> register(Registry<T> registry, String modId) {
    return new Provider<>(modId, registry);
  }

  private static class Provider<T> implements RegistryProvider<T> {
    private final String modId;
    private final Registry<T> registry;

    @SuppressWarnings({"unchecked"})
    private Provider(String modId, ResourceKey<? extends Registry<T>> key) {
      this.modId = modId;

      final var reg = BuiltInRegistries.REGISTRY.get(key.location());
      if (reg == null) {
        throw new RuntimeException("The Registry with name " + key.location() + " was not found!");
      }
      registry = (Registry<T>) reg;
    }

    private Provider(String modId, Registry<T> registry) {
      this.modId = modId;
      this.registry = registry;
    }

    @Override
    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
      final var rl = ResourceLocation.fromNamespaceAndPath(modId, name);
      final var obj = Registry.register(registry, rl, supplier.get());
        return new RegistryObject<>() {

            @Override
            public ResourceLocation getId() {
                return rl;
            }

            @Override
            public I get() {
                return obj;
            }
        };
    }
  }
}
