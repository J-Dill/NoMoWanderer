package nomowanderer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import nomowanderer.NoMoWandererConstants;
import nomowanderer.tileentity.NoSolicitingSignBlockEntity;

public class NoSolicitingSignBlockEntityRenderer implements
    BlockEntityRenderer<NoSolicitingSignBlockEntity> {
    private static final ResourceLocation SIGN_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/signs/oak.png");
    private static final ResourceLocation SIGN_EMERALD_TEXTURE = ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "textures/block/no_soliciting_sign_overlay.png");
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(NoMoWandererConstants.MODID, "no_soliciting_sign"), "main");
    private final SignRenderer.SignModel model;

    public NoSolicitingSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        model = new SignRenderer.SignModel(context.bakeLayer(MODEL_LAYER));
    }

    @Override
    public void render(NoSolicitingSignBlockEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn,
        MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        matrixStackIn.pushPose();
        if (blockstate.getBlock() instanceof StandingSignBlock) {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f1 = -((float)(blockstate.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
            this.model.stick.visible = true;
        } else {
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            float f4 = -blockstate.getValue(WallSignBlock.FACING).toYRot();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f4));
            matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);
            this.model.stick.visible = false;
        }

        // Render sign base, i.e. vanilla/resource pack texture.
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer consumerBase = bufferIn.getBuffer(this.model.renderType(SIGN_TEXTURE));
        this.model.root.render(matrixStackIn, consumerBase, combinedLightIn, combinedOverlayIn);
        this.model.stick.render(matrixStackIn, consumerBase, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();

        // Render the overlay over the base.
        matrixStackIn.pushPose();
        matrixStackIn.scale(.6F, -.6F, -1F);
        matrixStackIn.translate(0F, -0.072F, .02F);
        VertexConsumer consumerEmerald = bufferIn.getBuffer(this.model.renderType(SIGN_EMERALD_TEXTURE));
        this.model.root.render(matrixStackIn, consumerEmerald, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();

        // Finish up.
        matrixStackIn.popPose();
    }
}
