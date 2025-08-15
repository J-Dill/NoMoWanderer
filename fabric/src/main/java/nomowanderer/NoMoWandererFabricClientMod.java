package nomowanderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import nomowanderer.client.NoSolicitingSignBlockEntityRenderer;

public class NoMoWandererFabricClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerModels();

        BlockEntityRenderers.register(CommonRegistry.NO_SOLICITING_SIGN_BE.get(),
                NoSolicitingSignBlockEntityRenderer::new);

        BlockRenderLayerMap.putBlock(CommonRegistry.TRADER_RUG_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
    }

    public static void registerModels() {
        LayerDefinition standingSignDef = NoSolicitingSignBlockEntityRenderer.createSignLayer(true);
        LayerDefinition wallSignDef = SignRenderer.createSignLayer(false);
        EntityModelLayerRegistry.registerModelLayer(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER,
                () -> standingSignDef);
        EntityModelLayerRegistry.registerModelLayer(NoSolicitingSignBlockEntityRenderer.MODEL_LAYER_WALL,
                () -> wallSignDef);
    }

}
