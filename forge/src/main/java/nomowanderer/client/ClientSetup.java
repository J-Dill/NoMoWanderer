package nomowanderer.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.CommonRegistry;

import java.lang.invoke.MethodHandles;

public class ClientSetup {

    public static void init(FMLJavaModLoadingContext context) {

//        BusGroup modBusGroup = context.getModBusGroup();

        BusGroup.DEFAULT.register(MethodHandles.lookup(), ClientSetup.class);
//        eventBus.addListener(ClientSetup::clientOnlySetup);
//        eventBus.addListener(ClientSetup::registerBlockEntityRenderers);
//        eventBus.addListener(ClientSetup::registerModels);
    }

    @SubscribeEvent
    public static void clientOnlySetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Set render layer for trader rug block to enable transparency
            // TODO : This is a temporary solution for Forge since it is not using the JSON file correctly.
            ItemBlockRenderTypes.setRenderLayer(CommonRegistry.TRADER_RUG_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
        });
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CommonRegistry.NO_SOLICITING_SIGN_BE.get(), NoSolicitingSignBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        LayerDefinition standingSignDef = NoSolicitingSignBlockEntityRenderer.createSignLayer(true);
        LayerDefinition wallSignDef = SignRenderer.createSignLayer(false);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER, () -> standingSignDef);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER_WALL, () -> wallSignDef);
    }

}
