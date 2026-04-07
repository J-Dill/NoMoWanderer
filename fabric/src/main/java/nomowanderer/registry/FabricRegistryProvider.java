package nomowanderer.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import nomowanderer.registry.services.RegistryFactory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FabricRegistryProvider implements RegistryFactory {

  @Override
  public <T> RegistryProvider<T> register(ResourceKey<? extends @NotNull Registry<@NotNull T>> resourceKey,
                                          String modId) {
    return new Provider<>(modId, resourceKey);
  }

  @Override
  public <T> RegistryProvider<T> register(Registry<@NotNull T> registry, String modId) {
    return new Provider<>(modId, registry);
  }

  private static class Provider<T> implements RegistryProvider<T> {
    private final String modId;
    private final Registry<T> registry;

    @SuppressWarnings({"unchecked"})
    private Provider(String modId, ResourceKey<? extends Registry<T>> key) {
      this.modId = modId;

      final var reg = BuiltInRegistries.REGISTRY.get(key.identifier());
      if (reg.isEmpty()) {
        throw new RuntimeException("The Registry with name " + key.registry() + " was not found!");
      }
      registry = (Registry<@NotNull T>) reg.get().value();;
    }

    private Provider(String modId, Registry<@NotNull T> registry) {
      this.modId = modId;
      this.registry = registry;
    }

    @Override
    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
      final var rl = Identifier.fromNamespaceAndPath(modId, name);
      final var obj = Registry.register(registry, rl, supplier.get());
        return new RegistryObject<>() {

            @Override
            public Identifier getId() {
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
