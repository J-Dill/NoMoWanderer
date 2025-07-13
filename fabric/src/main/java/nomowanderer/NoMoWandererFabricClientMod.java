package nomowanderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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
                () -> createSignLayer(false));
        BlockEntityRenderers.register(CommonRegistry.NO_SOLICITING_SIGN_BE.get(),
                NoSolicitingSignBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(CommonRegistry.TRADER_RUG_BLOCK.get(), RenderType.translucent());
        ItemProperties.register(CommonRegistry.NO_SOLICITING_TALISMAN.get(),
                ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "enabled"),
                (stack, level, living, id) ->
                        AntiSolicitorTalismanItem.isEnabled(stack) ? 0.0F : 1.0F);
    }

    public static LayerDefinition createSignLayer(boolean p_368797_) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        if (p_368797_) {
            partdefinition.addOrReplaceChild(
                    "stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO
            );
        }

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

}
