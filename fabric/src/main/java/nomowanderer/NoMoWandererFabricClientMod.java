package nomowanderer;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import nomowanderer.client.renderer.blockentity.NoSolicitingSignBlockEntityRenderer;

public class NoMoWandererFabricClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(
                CommonRegistry.NO_SOLICITING_SIGN_BE.get(),
                NoSolicitingSignBlockEntityRenderer::new
        );
    }
}