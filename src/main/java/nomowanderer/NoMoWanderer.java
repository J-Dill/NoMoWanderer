package nomowanderer;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nomowanderer.blocks.ExampleTileEntityBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(NoMoWanderer.MODID)
public class NoMoWanderer {

    public static final String MODID = "nomowanderer";

    private static final Logger LOGGER = LogManager.getLogger(NoMoWanderer.MODID + " Mod Event Subscriber");

    public NoMoWanderer() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its Blocks.
     * This method will always be called before the Item registry method.
     */
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
        // Register all your blocks inside this registerAll call
        event.getRegistry().registerAll(
                // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Furnace
                setup(new ExampleTileEntityBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)), "example_tile_entity")
        );
        LOGGER.debug("Registered Blocks");
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
        Preconditions.checkNotNull(name, "Name to assign to entry cannot be null!");
        return setup(entry, new ResourceLocation(NoMoWanderer.MODID, name));
    }

    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) {
        Preconditions.checkNotNull(entry, "Entry cannot be null!");
        Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
        entry.setRegistryName(registryName);
        return entry;
    }
}