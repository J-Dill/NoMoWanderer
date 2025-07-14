package nomowanderer.client;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nomowanderer.CommonRegistry;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.items.AntiSolicitorTalismanItem;

public class ClientSetup {

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ClientSetup::clientOnlySetup);
        eventBus.addListener(ClientSetup::registerBlockEntityRenderers);
        eventBus.addListener(ClientSetup::registerModels);
    }

    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CommonRegistry.NO_SOLICITING_SIGN_BE.get(), NoSolicitingSignBlockEntityRenderer::new);
    }

    public static void clientOnlySetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(CommonRegistry.NO_SOLICITING_TALISMAN.get(),
                    ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "enabled"),
                    (stack, level, living, id) ->
                            AntiSolicitorTalismanItem.isEnabled(stack) ? 0.0F : 1.0F);
        });
    }

    public static void registerModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        LayerDefinition standingSignDef = NoSolicitingSignBlockEntityRenderer.createSignLayer(true);
        LayerDefinition wallSignDef = SignRenderer.createSignLayer(false);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER, () -> standingSignDef);
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER_WALL, () -> wallSignDef);
    }

}
