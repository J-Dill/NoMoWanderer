package nomowanderer.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.DeferredRegister;
import nomowanderer.registry.services.RegistryFactory;

import java.util.function.Supplier;

public class ForgeRegistryProvider implements RegistryFactory {

    @Override
    public <T> RegistryProvider<T> register(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find Mod container for mod: " + modId);
        if (containerOpt.get() instanceof FMLModContainer fmlModContainer) {
            final var register = DeferredRegister.create(resourceKey, modId);
            register.register(fmlModContainer.getEventBus());
            return new Provider<>(register);
        } else {
            throw new ClassCastException("The container for mod [" + modId + "] is not a FML one!");
        }
    }

    private record Provider<T>(DeferredRegister<T> registry) implements RegistryProvider<T> {

        @Override
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            final var obj = registry.<I>register(name, supplier);
            return new RegistryObject<>() {

                @Override
                public ResourceLocation getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return obj.get();
                }

            };
        }
    }
}
