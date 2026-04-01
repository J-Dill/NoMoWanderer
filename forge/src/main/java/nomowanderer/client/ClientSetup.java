package nomowanderer.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.CommonRegistry;
import nomowanderer.client.renderer.blockentity.NoSolicitingSignBlockEntityRenderer;

public class ClientSetup {

    public static void init(FMLJavaModLoadingContext context) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientSetup::registerBlockEntityRenderers);
        EntityRenderersEvent.RegisterLayerDefinitions.BUS.addListener(ClientSetup::registerModels);
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
