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
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.client.renderer.blockentity.state.NoSolicitingSignRenderState;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class NoSolicitingSignBlockEntityRenderer implements BlockEntityRenderer<NoSolicitingSignBlockEntity, NoSolicitingSignRenderState> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, "no_soliciting_sign"), "main");
    public static final ModelLayerLocation MODEL_LAYER_WALL = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, "no_soliciting_sign_wall"), "main");

    private static final SpriteId OVERLAY_SPRITE = new SpriteId(
            Sheets.SIGN_SHEET,
            Identifier.fromNamespaceAndPath(NoMoWandererConstants.MODID, "entity/signs/no_soliciting_sign_overlay")
    );

    private final Map<WoodType, Models> signModels;
    private final SpriteGetter sprites;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.signModels = WoodType.values().collect(ImmutableMap.toImmutableMap(
                type -> type,
                type -> new Models(
                        createSignModel(context.entityModelSet(), type, true),
                        createSignModel(context.entityModelSet(), type, false)
                )
        ));
        this.sprites = context.sprites();
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
        state.woodType = (blockState.getBlock() instanceof SignBlock signBlock)
                ? SignBlock.getWoodType(signBlock)
                : WoodType.OAK;

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
        Models models = this.signModels.getOrDefault(renderState.woodType, this.signModels.get(WoodType.OAK));
        Model.Simple model = renderState.isStanding ? models.standing() : models.wall();

        poseStack.pushPose();
        this.applySignTransform(poseStack, renderState.isStanding, renderState.rotationDegrees);
        this.submitSign(poseStack, renderState.lightCoords, renderState.woodType, model, renderState.breakProgress, nodeCollector);
        poseStack.popPose();
    }

    protected void submitSign(
            PoseStack poseStack,
            int lightCoords,
            WoodType woodType,
            Model.Simple model,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
            SubmitNodeCollector nodeCollector
    ) {
        // Sign Base
        poseStack.pushPose();
        float scale = this.getSignModelRenderScale();
        poseStack.scale(scale, -scale, -scale);
        SpriteId baseSprite = Sheets.getSignSprite(woodType);

        nodeCollector.submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                baseSprite,
                this.sprites,
                0,
                crumblingOverlay
        );
        poseStack.popPose();

        // Emerald Overlay
        poseStack.pushPose();
        poseStack.scale(0.6F, -0.6F, -1.0F);
        poseStack.translate(0.0F, -0.072F, 0.02F);

        nodeCollector.submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                OVERLAY_SPRITE,
                this.sprites,
                0,
                crumblingOverlay
        );
        poseStack.popPose();
    }

    public float getSignModelRenderScale() {
        return 0.6666667F;
    }

    private void applySignTransform(PoseStack poseStack, boolean isStanding, float rotationDegrees) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotationDegrees));

        if (!isStanding) {
            poseStack.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    public static Model.Simple createSignModel(EntityModelSet modelSet, WoodType woodType, boolean isStanding) {
        ModelLayerLocation layer = isStanding
                ? ModelLayers.createStandingSignModelName(woodType)
                : ModelLayers.createWallSignModelName(woodType);
        return new Model.Simple(modelSet.bakeLayer(layer), RenderTypes::entityCutout);
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

    record Models(Model.Simple standing, Model.Simple wall) {}
}