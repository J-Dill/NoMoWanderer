package nomowanderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nomowanderer.tileentity.NoSolicitingSignBlockEntityRenderer;
import nomowanderer.util.SpawnTraderCommand;


@Mod(NoMoWanderer.MODID)
public class NoMoWanderer {

    public static final String MODID = "nomowanderer";

    public NoMoWanderer() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("nomowanderer-common.toml"));

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        Registry.BLOCKS.register(modEventBus);
        Registry.ITEMS.register(modEventBus);
        Registry.BLOCK_ENTITIES.register(modEventBus);
    }

    @SubscribeEvent
    public void registerModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER, SignRenderer::createSignLayer);
    }

    public void registerCommands(RegisterCommandsEvent event) {
        SpawnTraderCommand.create(event.getDispatcher());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registry.TRADER_RUG_BLOCK.get(), RenderType.cutout());
    }

}