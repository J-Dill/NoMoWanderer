package nomowanderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import nomowanderer.client.NoSolicitingSignBlockEntityRenderer;
import nomowanderer.items.AntiSolicitorTalismanItem;

public class NoMoWandererFabricClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER,
                SignRenderer::createSignLayer);
        BlockEntityRenderers.register(CommonRegistry.NO_SOLICITING_SIGN_BE.get(),
                NoSolicitingSignBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(CommonRegistry.TRADER_RUG_BLOCK.get(), RenderType.translucent());
        ItemProperties.register(CommonRegistry.NO_SOLICITING_TALISMAN.get(),
                ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "enabled"),
                (stack, level, living, id) ->
                        AntiSolicitorTalismanItem.isEnabled(stack) ? 0.0F : 1.0F);
    }

}
