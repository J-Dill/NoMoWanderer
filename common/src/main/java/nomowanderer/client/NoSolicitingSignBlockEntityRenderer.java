package nomowanderer.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;

import java.util.Map;

public class NoSolicitingSignBlockEntityRenderer implements
    BlockEntityRenderer<NoSolicitingSignBlockEntity> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
            NoMoWandererConstants.MODID, "no_soliciting_sign"), "main");
    public static final ModelLayerLocation MODEL_LAYER_WALL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
            NoMoWandererConstants.MODID, "no_soliciting_sign_wall"), "main");

    private static final ResourceLocation SIGN_EMERALD_TEXTURE = ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "textures/block/no_soliciting_sign_overlay.png");

    private final Map<WoodType, NoSolicitingSignBlockEntityRenderer.Models> signModels;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.signModels = ImmutableMap.of(
            WoodType.OAK,
            new NoSolicitingSignBlockEntityRenderer.Models(
                createSignModel(context.getModelSet(), WoodType.OAK, true),
                createSignModel(context.getModelSet(), WoodType.OAK, false)
            )
        );
    }

    public void render(NoSolicitingSignBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 vec3) {
        BlockState blockstate = blockEntity.getBlockState();
        SignBlock signblock = (SignBlock)blockstate.getBlock();
        WoodType woodtype = SignBlock.getWoodType(signblock);
        NoSolicitingSignBlockEntityRenderer.Models signrenderer$models = this.signModels.get(woodtype);
        Model model = blockstate.getBlock() instanceof StandingSignBlock ? signrenderer$models.standing() : signrenderer$models.wall();
        this.renderSign(poseStack, bufferSource, packedLight, packedOverlay, blockstate, signblock, woodtype, model);
    }

    public float getSignModelRenderScale() {
        return 0.6666667F;
    }

    void renderSign(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            BlockState blockState,
            SignBlock signBlock,
            WoodType woodType,
            Model model
    ) {
        poseStack.pushPose();
        this.translateSign(poseStack, -signBlock.getYRotationDegrees(blockState), blockState);
        this.renderSign(poseStack, bufferSource, packedLight, packedOverlay, woodType, model);
        poseStack.popPose();
    }

    void translateSign(PoseStack poseStack, float rotationDegrees, BlockState blockState) {
        poseStack.translate(0.5F, 0.75F * this.getSignModelRenderScale(), 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));
        if (!(blockState.getBlock() instanceof StandingSignBlock)) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    void renderSign(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
                    WoodType woodType, Model model) {
        // Render sign base.
        poseStack.pushPose();
        float f = this.getSignModelRenderScale();
        poseStack.scale(f, -f, -f);
        Material material = this.getSignMaterial(woodType);
        VertexConsumer vertexconsumer = material.buffer(bufferSource, model::renderType);
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();

        // Render the overlay over the base.
        poseStack.pushPose();
        poseStack.scale(.6F, -.6F, -1F);
        poseStack.translate(0F, -0.072F, .02F);
        VertexConsumer consumerEmerald = bufferSource.getBuffer(model.renderType(SIGN_EMERALD_TEXTURE));
        model.renderToBuffer(poseStack, consumerEmerald, packedLight, packedOverlay);
        poseStack.popPose();
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
