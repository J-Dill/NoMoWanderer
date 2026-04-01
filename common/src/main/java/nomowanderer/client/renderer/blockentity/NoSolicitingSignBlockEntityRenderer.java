package nomowanderer.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.client.renderer.blockentity.state.NoSolicitingSignRenderState;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class NoSolicitingSignBlockEntityRenderer implements
    BlockEntityRenderer<NoSolicitingSignBlockEntity, NoSolicitingSignRenderState> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
            NoMoWandererConstants.MODID, "no_soliciting_sign"), "main");
    public static final ModelLayerLocation MODEL_LAYER_WALL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
            NoMoWandererConstants.MODID, "no_soliciting_sign_wall"), "main");

    private static final ResourceLocation SIGN_EMERALD_TEXTURE = ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "textures/block/no_soliciting_sign_overlay.png");

    private final Map<WoodType, NoSolicitingSignBlockEntityRenderer.Models> signModels;
    private final MaterialSet materials;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.signModels = ImmutableMap.of(
            WoodType.OAK,
            new NoSolicitingSignBlockEntityRenderer.Models(
                createSignModel(context.entityModelSet(), WoodType.OAK, true),
                createSignModel(context.entityModelSet(), WoodType.OAK, false)
            )
        );
        this.materials = context.materials();
    }

    @Override
    public @NotNull NoSolicitingSignRenderState createRenderState() {
        return new NoSolicitingSignRenderState();
    }

    @Override
    public void submit(NoSolicitingSignRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        BlockState blockstate = renderState.blockState;
        SignBlock signblock = (SignBlock)blockstate.getBlock();
        WoodType woodtype = SignBlock.getWoodType(signblock);
        NoSolicitingSignBlockEntityRenderer.Models signrenderer$models = this.signModels.get(woodtype);
        Model model = blockstate.getBlock() instanceof StandingSignBlock ? signrenderer$models.standing() : signrenderer$models.wall();
        this.submitSignWithText(renderState, poseStack, blockstate, signblock, woodtype, model, renderState.breakProgress, nodeCollector);
    }

    private void submitSignWithText(NoSolicitingSignRenderState signRenderState, PoseStack poseStack, BlockState blockState, SignBlock signBlock, WoodType woodType, Model simple, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, SubmitNodeCollector submitNodeCollector) {
        poseStack.pushPose();
        this.translateSign(poseStack, -signBlock.getYRotationDegrees(blockState), blockState);
        this.submitSign(poseStack, signRenderState.lightCoords, woodType, simple, crumblingOverlay, submitNodeCollector);
        poseStack.popPose();
    }

    protected void submitSign(PoseStack poseStack, int i, WoodType woodType, Model simple, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, SubmitNodeCollector submitNodeCollector) {
        // Render sign base.
        poseStack.pushPose();
        float f = this.getSignModelRenderScale();
        poseStack.scale(f, -f, -f);
        Material material = this.getSignMaterial(woodType);
        Objects.requireNonNull(simple);
        RenderType renderType = material.renderType(simple::renderType);
        submitNodeCollector.submitModel(simple, Unit.INSTANCE, poseStack, renderType, i, OverlayTexture.NO_OVERLAY, -1, this.materials.get(material), 0, crumblingOverlay);
        poseStack.popPose();

        // Render emerald overlay over the sign face.
        poseStack.pushPose();
        poseStack.scale(0.6F, -0.6F, -1.0F);
        poseStack.translate(0.0F, -0.072F, 0.02F);
        RenderType overlayRenderType = RenderType.entityCutoutNoCull(SIGN_EMERALD_TEXTURE);
        submitNodeCollector.submitModel(simple, Unit.INSTANCE, poseStack, overlayRenderType, i, OverlayTexture.NO_OVERLAY, -1, null, 0, crumblingOverlay);
        poseStack.popPose();
    }

    public float getSignModelRenderScale() {
        return 0.6666667F;
    }

    void translateSign(PoseStack poseStack, float rotationDegrees, BlockState blockState) {
        poseStack.translate(0.5F, 0.75F * this.getSignModelRenderScale(), 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));
        if (!(blockState.getBlock() instanceof StandingSignBlock)) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    Material getSignMaterial(WoodType woodType) {
        return Sheets.getSignMaterial(woodType);
    }

    public static Model createSignModel(EntityModelSet modelSet, WoodType woodType, boolean isStanding) {
        ModelLayerLocation modelLayerLocation = isStanding ? ModelLayers.createStandingSignModelName(woodType) :
                ModelLayers.createWallSignModelName(woodType);
        return new Model.Simple(modelSet.bakeLayer(modelLayerLocation), RenderType::entityCutoutNoCull);
    }

    record Models(Model standing, Model wall) {}

    public static LayerDefinition createSignLayer(boolean isStanding) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        if (isStanding) {
            partdefinition.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        }

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

}
