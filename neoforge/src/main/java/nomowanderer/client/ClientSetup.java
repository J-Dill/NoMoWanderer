package nomowanderer.client;

import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import nomowanderer.CommonRegistry;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.items.AntiSolicitorTalismanItem;

public class ClientSetup {

    public static void init(IEventBus eventBus) {
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
                    new ResourceLocation(NoMoWandererConstants.MODID, "enabled"),
                    (stack, level, living, id) ->
                            AntiSolicitorTalismanItem.isEnabled(stack) ? 0.0F : 1.0F);
        });
    }

    public static void registerModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        definitions.registerLayerDefinition(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER, SignRenderer::createSignLayer);
    }

}
