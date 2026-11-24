package nomowanderer.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.client.renderer.blockentity.state.NoSolicitingSignRenderState;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoSolicitingSignBlockEntityRenderer implements BlockEntityRenderer<NoSolicitingSignBlockEntity, NoSolicitingSignRenderState> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, "no_soliciting_sign"), "main");
    public static final ModelLayerLocation MODEL_LAYER_WALL = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, "no_soliciting_sign_wall"), "main");

    private static final Identifier SIGN_EMERALD_TEXTURE = Identifier.fromNamespaceAndPath(
            NoMoWandererConstants.MODID, "textures/block/no_soliciting_sign_overlay.png");

    private final Model.Simple overlayModel;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.overlayModel = new Model.Simple(createSignLayer(false).bakeRoot(), RenderTypes::entityCutout);
    }

    @Override
    public @NotNull NoSolicitingSignRenderState createRenderState() {
        return new NoSolicitingSignRenderState();
    }

    @Override
    public void extractRenderState(
            NoSolicitingSignBlockEntity blockEntity,
            NoSolicitingSignRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        // Read the actual block state here from the BlockEntity
        BlockState blockState = blockEntity.getBlockState();
        state.isStanding = blockState.getBlock() instanceof StandingSignBlock;

        if (state.isStanding && blockState.hasProperty(StandingSignBlock.ROTATION)) {
            state.rotationDegrees = RotationSegment.convertToDegrees(blockState.getValue(StandingSignBlock.ROTATION));
        } else if (blockState.hasProperty(WallSignBlock.FACING)) {
            state.rotationDegrees = blockState.getValue(WallSignBlock.FACING).toYRot();
        } else {
            state.rotationDegrees = 0.0F;
        }
    }

    @Override
    public void submit(
            NoSolicitingSignRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            CameraRenderState cameraRenderState
    ) {
        poseStack.pushPose();
        this.applySignTransform(poseStack, renderState.isStanding, renderState.rotationDegrees);
        this.submitOverlay(poseStack, renderState.lightCoords, nodeCollector);
        poseStack.popPose();
    }

    protected void submitOverlay(
            PoseStack poseStack,
            int lightCoords,
            SubmitNodeCollector nodeCollector
    ) {
        // Emerald Overlay (the wooden sign base is rendered via block models in 26.2+)
        poseStack.pushPose();
        poseStack.scale(0.6F, -0.6F, -1.0F);
        poseStack.translate(0.0F, -0.072F, 0.02F);

        nodeCollector.submitModel(
                this.overlayModel,
                Unit.INSTANCE,
                poseStack,
                RenderTypes.entityCutout(SIGN_EMERALD_TEXTURE),
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );
        poseStack.popPose();
    }

    private void applySignTransform(PoseStack poseStack, boolean isStanding, float rotationDegrees) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.rotateDegrees(Axis.YP, -rotationDegrees);

        if (!isStanding) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    public static LayerDefinition createSignLayer(boolean isStanding) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("sign", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
        if (isStanding) {
            root.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
        }
        return LayerDefinition.create(mesh, 64, 32);
    }
}