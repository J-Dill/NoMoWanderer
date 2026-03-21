package nomowanderer.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.CommonRegistry;

import java.lang.invoke.MethodHandles;

public class ClientSetup {

    public static void init(FMLJavaModLoadingContext context) {
        BusGroup modBusGroup = context.getModBusGroup();
        modBusGroup.register(MethodHandles.lookup(), ClientSetup.class);
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
