package nomowanderer.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import nomowanderer.CommonRegistry;

public class ClientSetup {

    public static void init(IEventBus eventBus) {
        eventBus.addListener(ClientSetup::registerBlockEntityRenderers);
        eventBus.addListener(ClientSetup::registerModels);
    }

    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CommonRegistry.NO_SOLICITING_SIGN_BE.get(), NoSolicitingSignBlockEntityRenderer::new);
    }

    public static void registerModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        LayerDefinition standingSignDef = NoSolicitingSignBlockEntityRenderer.createSignLayer(true);
        LayerDefinition wallSignDef = SignRenderer.createSignLayer(false);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER, () -> standingSignDef);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER_WALL, () -> wallSignDef);
    }

}
